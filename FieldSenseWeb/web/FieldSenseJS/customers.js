/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var lat = 0;
var lng = 0;
var industryLength = 0;
var territoryLength = 0;
var doubleClickFlag = true;
var recentCustomers = new Array();
var industryCustomers = new Array();
var territorycustomers = new Array();
var customer = [];
var customerRecordsOffsetFlag = 0;
var isAssigneeIsImmidiateSubOrdinate = false;
var pathName;
//added by nikhil
var loginUserIdcookie = fieldSenseGetCookie("loginUserIdcookie");
var loginUserNamecookie = fieldSenseGetCookie("loginUserNamecookie");
var accountIdcookie = fieldSenseGetCookie("accountIdcookie");
var accountNamecookie = fieldSenseGetCookie("accountNamecookie");
var UserRolecookie = fieldSenseGetCookie("UserRolecookie");
console.log("accountNamecookie " + accountNamecookie);
//ended by nikhil
var intervalCustomer = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
   // console.log("userToken2 "+userToken2);

    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {


            loggedinUserImageData();
            loggedinUserData();
            var loc = window.location;
            pathName = loc.pathname.substring(loc.pathname.lastIndexOf('/') + 1).trim();
            if (pathName == "add_customer.html" || pathName == "add_customerAdmin.html") {
                getActiveTerritories();
                getActiveIndustries();
            } else if (pathName == "adm_import_cnt.html" || pathName == "adm_import_cst.html") {
                if(pathName == "adm_import_cnt.html")
                {    
//                clevertap.event.push("Import Contact", {
////                    "UserId": loginUserIdcookie,
////                    "User Name": loginUserNamecookie,
////                    "AccountId": accountIdcookie,
//                    "Source": "Web",
//                    "Account name": accountNamecookie,
//                    "UserRolecookie": UserRolecookie,
//                });
            }else if(pathName == "adm_import_cst.html")
            {
//                clevertap.event.push("Import Customers", {
////                    "UserId": loginUserIdcookie,
////                    "User Name": loginUserNamecookie,
////                    "AccountId": accountIdcookie,
//                    "Source": "Web",
//                    "Account name": accountNamecookie,
//                    "UserRolecookie": UserRolecookie,
//                });
            }
                downloadSampleCustomersFile();
            } else {
                //alert(" and "+pathName);
                leftSideMenu();
                showCustomers();
            }
            ;

            window.clearTimeout(intervalCustomer);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
function showCustomers() {
    //    document.getElementById("id_searchText").focus();
    recentCustomers = new Array();
    industryCustomers = new Array();
    territorycustomers = new Array();
    selectedTerritories = new Array();
    $(".checkboxes").each(function () {
        $(this).attr('checked', false);
        $(this).parent().removeClass('checked');
    });
    $("[id^=id_Territory_]").each(function () {
        $(this).attr('checked', false);
        $(this).parent().removeClass('checked');
    });
    $(".cls_clearRecent").hide();
    $(".cls_clearTerritory").hide();
    $(".cls_clearIndustry").hide();
    var searchText = $('#id_searchText').val().trim();
    
    var cust_url = fieldSenseURL + "/customer/details/" + loginUserId + "?searchText=" + searchText;
    if (role == 1 || role == 3 || role == 6 || role == 8) {
        cust_url = fieldSenseURL + "/customer/details?searchText=" + searchText
    }
    
    var start = 0;
    var length = 20;
    myTable = $("#sample_5").DataTable();
    if (myTable != null)
        myTable.destroy();
    //$('#pleaseWaitDialog').modal('show');
    $('#id_customerdetails').html('');
    var customerShowTemplateHtml = '';
    customerShowTemplateHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
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
    customerShowTemplateHtml += '<tbody></tbody></table>';
    $("#id_customerdetails").html(customerShowTemplateHtml);
    var inboxtable = $('#sample_5').dataTable({
        "bServerSide": true,
        "bDestroy": true,
        "ajax": {
            "url": cust_url,
            "type": "GET",
            headers: {
                "userToken": userToken
            },
            contentType: "application/json; charset=utf-8",
            crossDomain: false,
            cache: false,
            dataType: 'json',
            asyn: true,
            error: function (data) {
                //$('#pleaseWaitDialog').modal('hide');
                if (data.status == 200) {
                    fieldSenseTosterError("Some problem occured.", true);
                    showCustomers();
                } else {
                    $('#pleaseWaitDialog').modal('hide')
                    fieldSenseTosterError(data.responseJSON.errorMessage, true);
                }
                //logout();
            }
        },
        "iDisplayStart": start,
        // set the initial value
        "iDisplayLength": length, // It will be by default page width set by user
        "sPaginationType": "bootstrap_full_number",
        "bProcessing": false,
        "aLengthMenu": [
            [5, 10, 15, 20, 50, 100],
            [5, 10, 15, 20, 50, 100] // change per page values here
        ],
        "oLanguage": {
            // "sProcessing":"Loading",
            "sEmptyTable": "No data available in table",
            "sLengthMenu": "_MENU_ <span id ='id_totalCustRecords'></span>",
            //"sInfo": "Showing _START_ to _END_ of _TOTAL_ Mails",
            "oPaginate": {
                "sPrevious": "Prev",
                "sNext": "Next"
            }
        },
        "aoColumnDefs": [
            {"bSortable": false,
                "aTargets": [1, 5]

            }
        ],
        "aoColumns": [// ahmed : here we populate the data in datatable that is returned using Ajax
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.customerName;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.customerPhone1;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.customerEmail1;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    return full.industry;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    return full.territory;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    // Added by manohar, Bug #28019
                    var custPrintNameAs = full.customerPrintas;
                    var customerName = full.customerName;
                    if(custPrintNameAs.includes("&apos;")){
                        var arrayCustName = custPrintNameAs.split("&apos;");
                        custPrintNameAs = arrayCustName[1];
                    }
                    if(customerName.includes("&apos;")){
                        var arrayCustName = customerName.split("&apos;");
                        customerName = arrayCustName[1];
                    }
                    var actionButtons = '<button type="button" class="btn btn-default btn-xs tooltips"  data-placement="bottom" title="Edit" onclick="editCustomerDetails(\'' + full.id + '\',\'' + custPrintNameAs + '\');return false;"><i class="fa fa-edit"></i></button>';
                    if (pathName != "customers.html") {
                        actionButtons += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteCustomer(\'' + full.id + '\',\'' + customerName + '\',\'' + full.customersCount + '\')"><i class="fa fa-trash-o"></i></button>';
                    }
                    return actionButtons;
                }
            }
        ],
        "aaSorting": [],
        "bFilter": false,
        "preDrawCallback": function (settings) {
            $('#pleaseWaitDialog').modal('show');
        },
        "fnDrawCallback": function (oSettings) {
            if (oSettings.fnDisplayEnd() > 0) {
                $("#id_totalCustRecords").html("<b>" + (oSettings._iDisplayStart + 1) + "</b> - <b>" + oSettings.fnDisplayEnd() + "</b> of <b>" + oSettings.fnRecordsTotal() + "</b> records");
            }
            inboxtable.$('td').mouseover(function () {
                var sData = $(this).text();
                $(this).attr('title', sData);
            });
            App.initUniform();
            App.fixContentHeight();
            jQuery('.tooltips').tooltip();
            $('#pleaseWaitDialog').modal('hide');
        },
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown	


    $('#id_searchText').val("");
}

function quoteAndEscape(str) {
    return ''
        + '&#39;'                      // open quote '
        + (''+str)                     // force string
            .replace(/\\/g, '\\\\')    // double \
            .replace(/"/g, '\\&quot;') // encode "
            .replace(/'/g, '\\&#39;')  // encode '
        + '&#39;';                     // close quote '
}

function createCustomerTemplate() {
    lat = 0;
    lng = 0;
}
function createCustomer() {
    var customerName = document.getElementById("id_customerName").value.trim();
    if (customerName.trim().length == 0) {
        fieldSenseTosterError("Customer Name cannot be empty", true);
        return false;
    }
    if (customerName.trim().length > 200) {
        fieldSenseTosterError("Customer name can not be more than 200 characters", true);
        return false;
    }
    var customerPrintAs = document.getElementById("id_customerPrintAs").value.trim();
    var customerLocationIdentifier = document.getElementById("id_customerLocation").value.trim();
    var isHeadOffice = document.getElementById("id_isHeadOffice").checked;
    // var territory = document.getElementById("id_territory").value.trim();
    var territory = $("#drilldown1 > span").text().trim();
    if (territory === "Select") {
        fieldSenseTosterError("Select Territory Category ", true);
        return false;
    }
    territory = $("#drilldown1 > span > span").text().trim();
    var industry = document.getElementById("id_industry").value.trim();
    var address = document.getElementById("id_address").value.trim();
    var phone1 = document.getElementsByClassName("cls_phone1")[0].value.trim();
    var phone2 = document.getElementsByClassName("cls_phone2")[0].value.trim();
    var phone3 = document.getElementsByClassName("cls_phone3")[0].value.trim();
    var fax1 = document.getElementById("id_fax1").value.trim();
    var fax2 = document.getElementById("id_fax2").value.trim();
    var email1 = document.getElementsByClassName("cls_email1")[0].value.trim();
    var email2 = document.getElementsByClassName("cls_email2")[0].value.trim();
    var email3 = document.getElementsByClassName("cls_email3")[0].value.trim();
    var website1 = document.getElementById("id_website1").value.trim();
    var website2 = document.getElementById("id_website2").value.trim();


    var re = new RegExp("^[^!@#$%^&*~]");
    if (!(re.test(customerName))) {
        fieldSenseTosterError("Please enter valid customer name", true);
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
    if (customerLocationIdentifier.trim().length == 0) {
        fieldSenseTosterError("Location identifier cannot be empty", true);
        return false;
    }
    if (customerLocationIdentifier.trim().length != 0) {
        if (customerLocationIdentifier.trim().length > 200) {
            fieldSenseTosterError("Location identifier can not be more than 200 characters", true);
            return false;
        }
    } else {
        fieldSenseTosterError("Location identifier can not be empty", true);
        return false;
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
        if ((isNaN(phone1))) {
            fieldSenseTosterError("Please enter valid phone number", true);
            return false;
        }
    }
    if (phone2.trim().length != 0) {
        if ((isNaN(phone2))) {
            fieldSenseTosterError("Please enter valid phone number", true);
            return false;
        }
    }
    if (phone3.trim().length != 0) {
        if ((isNaN(phone3))) {
            fieldSenseTosterError("Please enter valid phone number", true);
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
    if (email2.trim().length != 0) {
        if (email2.trim().length > 100) {
            fieldSenseTosterError("Email2 can not be more than 100 characters", true);
            return false;
        }
    }
    if (email3.trim().length != 0) {
        if (email3.trim().length > 100) {
            fieldSenseTosterError("Email3 can not be more than 100 characters", true);
            return false;
        }
    }
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
    if (industry.trim() === "Select") {
        fieldSenseTosterError("Select Industry Category ", true);
        return false;
    }

    /*if (territory.trim().length != 0) {
     if (territory.trim().length > 100) {
     fieldSenseTosterError("Territory can not be more than 100 characters", true);
     return false;
     }
     }
     if (industry.trim().length != 0) {
     if (industry.trim().length > 100) {
     fieldSenseTosterError("Industry can not be more than 100 characters", true);
     return false;
     }
     }*/
    customerName = htmlEntities(customerName);
    customerPrintAs = htmlEntities(customerPrintAs);
    customerLocationIdentifier = htmlEntities(customerLocationIdentifier);
    territory = htmlEntities(territory);
    industry = htmlEntities(industry);
    address = htmlEntities(address);
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
        "customerType": 1,
        "created_by": {
            "id": loginUserId
        }
    }
    $('#pleaseWaitDialog').modal('show');
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
                //added by nikhil
                console.log("add coustmer :" + customerData.industry + " " + customerData.territory);
                clevertap.event.push(" Add Customer", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                    "Territory": customerData.territory,
                    "Industry": customerData.territory,
                });
                //ended by nikhil

                var customerId = customerData.id;
                $('#Contacts').css("display", "block");
                $('#Notes').css("display", "block");
                $('#Activity').css("display", "block");
                $('.editCust').html('<button type="submit" class="btn btn-info" tabindex="17" onClick="editCustomer(' + customerId + ');return false;">Save</button>');
                $('.custId').html('<button type="button" class="btn btn-info" tabindex="17" onclick="createContact(\'' + customerId + '\',\'' + customerPrintAs + '\');return false;">Save</button>');
                $('#id_notesSave').html('<button type="button" class="btn btn-info" tabindex="17" onClick="createNotes(' + customerId + ')">Save</button>');
                $('#id_addActivityTemplate').html('<button type="button" class="btn btn-sm btn-info" data-toggle="modal" href="#responsive4" onclick="addActivityInCustomerTemplate(\'' + customerId + '\',\'' + customerName + '\')"><i class="fa fa-plus"></i> Add New</button>');
                // fieldSenseTosterSuccess("Customer saved successfully, please add contact(s)", true);Customer added successfully. Please add Contact(s).
                fieldSenseTosterSuccess("Customer added successfully. Please add Contact(s)", true);
                $('#pleaseWaitDialog').modal('hide');
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}
function createContact(customerId, customerPrintAs) {
    if (doubleClickFlag) {
        var firstName = document.getElementById("id_firstname").value.trim();
        var middleName = document.getElementById("id_middlename").value.trim();
        var lastName = document.getElementById("id_lastname").value.trim();
        var phone1 = document.getElementsByClassName("cls_phonec1")[0].value.trim();
        var phone2 = document.getElementsByClassName("cls_phonec2")[0].value.trim();
        var phone3 = document.getElementsByClassName("cls_phonec3")[0].value.trim();
        var fax1 = document.getElementsByClassName("cls_faxc1")[0].value.trim();
        var fax2 = document.getElementsByClassName("cls_faxc2")[0].value.trim();
        var mobile1 = document.getElementsByClassName("cls_mobile1")[0].value.trim();
        var mobile2 = document.getElementsByClassName("cls_mobile2")[0].value.trim();
        var email1 = document.getElementsByClassName("cls_emailc1")[0].value.trim();
        var email2 = document.getElementsByClassName("cls_emailc2")[0].value.trim();
        var reportsTo = document.getElementById("id_reportsTo").value.trim();
        var assistancenm = document.getElementById("id_assistancenm").value.trim();
        var spousenm = document.getElementById("id_spousenm").value.trim();
        var birthdate = document.getElementById("id_birthdate").value.trim();
        var anniversarydate = document.getElementById("id_anniversarydate").value.trim();
        var designation = document.getElementById("id_designation").value.trim();
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
        }
        else {
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
        /*if (middleName.trim().length == 0) {
         fieldSenseTosterError("Middle Name cannot be empty", true);
         return false;
         }*/
        if (middleName.trim().length != 0) {
            if (middleName.trim().length > 50) {
                fieldSenseTosterError("Middle name can not be more than 50 characters", true);
                return false;
            }
        }
        if (lastName.trim().length == 0) {
            fieldSenseTosterError("Last Name cannot be empty", true);
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
            if ((isNaN(phone1))) {
                fieldSenseTosterError("Please enter valid phone number", true);
                return false;
            }
        }
        if (phone2.trim().length != 0) {
            if (phone2.trim().length > 10) {
                fieldSenseTosterError("Phone2 can not be more than 10 digits", true);
                return false;
            }
            if ((isNaN(phone2))) {
                fieldSenseTosterError("Please enter valid phone number", true);
                return false;
            }
        }
        if (phone3.trim().length != 0) {
            if (phone3.trim().length > 10) {
                fieldSenseTosterError("Phone3 can not be more than 10 digits", true);
                return false;
            } else if ((isNaN(phone3))) {
                fieldSenseTosterError("Please enter valid phone number", true);
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
        // Added by nikhil
        if (email1 != "") {
            var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
            if (!emailPattern.test(email1)) {
                fieldSenseTosterError("Please enter valid email1 .", true);
                return false;
            }
            // Ended by nikhil
            if (email1.trim().length != 0) {
                if (email1.trim().length > 100) {
                    fieldSenseTosterError("Email1 can not be more than 100 characters", true);
                    return false;
                }
            }
        }
        if (email2 != "") {
            // Added by nikhil
            if (!emailPattern.test(email2)) {
                fieldSenseTosterError("Please enter valid email2 .", true);
                return false;
            }
        }
        // Ended by nikhil
        if (email2.trim().length != 0) {
            if (email2.trim().length > 100) {
                fieldSenseTosterError("Email2 can not be more than 100 characters", true);
                return false;
            }
        }
    }
//         var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
//    if (!emailPattern.test(email2)) {
//        fieldSenseTosterError("Please enter valid email2 .", true);
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
    doubleClickFlag = false;
    $('#pleaseWaitDialog').modal('show');
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
            clevertap.event.push(" Add Contact", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                "Source": "Web",
                "Account name": accountNamecookie,
                "UserRolecookie": UserRolecookie,
            });
            var customerContactData = data.result;
            if (data.errorCode == '0000') {
                $(".cls_addContactInEditTemplate").modal('hide');
                var contactId = customerContactData.id;
                $('#id_firstname').val('');
                $('#id_middlename').val('');
                $('#id_lastname').val('');
                $('.cls_phonec1').val('');
                $('.cls_phonec2').val('');
                $('.cls_phonec3').val('');
                $('.cls_faxc1').val('');
                $('.cls_faxc2').val('');
                $('.cls_mobile1').val('');
                $('.cls_mobile2').val('');
                $('.cls_emailc1').val('');
                $('.cls_emailc2').val('');
                $('#id_reportsTo').val('');
                $('#id_assistancenm').val('');
                $('#id_spousenm').val('');
                $('#id_birthdate').val('');
                $('#id_anniversarydate').val('');
                $('#id_designation').val('');
                doubleClickFlag = true;
                $.ajax({
                    type: "GET",
                    url: fieldSenseURL + "/customerContact/" + contactId,
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
                            var contactTemplate = ''
                            var firstName = contactData.firstName;
                            var middleName = contactData.middleName;
                            var lastName = contactData.lastName;
                            var mobile1 = contactData.mobile1;
                            var email1 = contactData.email1;
                            var fullName = firstName + ' ' + middleName + ' ' + lastName;
                            contactTemplate += '<tr>';
                            contactTemplate += '<td>';
                            contactTemplate += '' + fullName + '';
                            contactTemplate += '</td>';
                            contactTemplate += '<td>';
                            contactTemplate += '' + mobile1 + '';
                            contactTemplate += '</td>';
                            contactTemplate += '<td>';
                            contactTemplate += email1;
                            contactTemplate += '</td>';
                            contactTemplate += '<td>';
                            contactTemplate += '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#responsive2" data-placement="bottom" title="Edit" onclick="editContactTemplate(\'' + contactId + '\',\'' + customerId + '\')"><i class="fa fa-edit"></i></button>';
                            contactTemplate += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteCustomerContact(\'' + contactId + '\',\'' + fullName + '\',\'' + customerId + '\')"><i class="fa fa-trash-o"></i></button>									';
                            contactTemplate += '</td>';
                            contactTemplate += '</tr>';
                            $('#id_viewContactTemplate').append(contactTemplate);
                            $('#pleaseWaitDialog').modal('hide');
                        }
                    }
                });
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
                doubleClickFlag = true;
            }
        }
    });
}
function showOneCustomer(customerId) {
    $('#custContacttmt').html('');
    $('#pleaseWaitDialog').modal('show');
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
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
    $('#pleaseWaitDialog').modal('show');
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
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
    $('#pleaseWaitDialog').modal('show');
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
                    var dateTime = activityData[i].dateTime;
                    var sdateTime = activityData[i].sdateTime;
                    var contact = firstName + ' ' + middleName + ' ' + lastName;
                    var activityDateTime = fieldSenseseForJSDate(sdateTime);
                    var status = activityData[i].status;
                    var statucIs;
                    if (status == 0) {
                        statucIs = "Pending";
                    } else if (status == 1) {
                        statucIs = "In Progress";
                    } else if (status == 2) {
                        statucIs = "Completed";
                    }
                    else if (status == 3) {
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
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}
function showOneCustomerContact(customerContactId) {
    $('#pleaseWaitDialog').modal('show');
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
                if (bdate == "01-01-1800") {
                    bdate = '';
                }
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
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}
function editCustomer(customerId) {
    var customerName = document.getElementById("id_customerName").value.trim();
    var customerPrintAs = document.getElementById("id_customerPrintAs").value.trim();
    var customerLocationIdentifier = document.getElementById("id_customerLocation").value.trim();
    var isHeadOffice = document.getElementById("id_isHeadOffice").checked;
    var territory = document.getElementById("id_territory").value.trim();
    var industry = document.getElementById("id_industry").value.trim();
    var address = document.getElementById("id_address").value.trim();
    var phone1 = document.getElementsByClassName("cls_phone1")[0].value.trim();
    var phone2 = document.getElementsByClassName("cls_phone2")[0].value.trim();
    var phone3 = document.getElementsByClassName("cls_phone3")[0].value.trim();
    var fax1 = document.getElementById("id_fax1").value.trim();
    var fax2 = document.getElementById("id_fax2").value.trim();
    var email1 = document.getElementsByClassName("cls_email1")[0].value.trim();
    var email2 = document.getElementsByClassName("cls_email2")[0].value.trim();
    var email3 = document.getElementsByClassName("cls_email3")[0].value.trim();
    var website1 = document.getElementById("id_website1").value.trim();
    var website2 = document.getElementById("id_website2").value.trim();
    if (customerName.trim().length == 0) {
        fieldSenseTosterError("Customer Name cannot be empty", true);
        return false;
    }
    if (customerName.trim().length > 200) {
        fieldSenseTosterError("Customer name can not be more than 200 characters", true);
        return false;
    }
    var re = new RegExp("^[^!@#$%^&*~]");
    if (!(re.test(customerName))) {
        fieldSenseTosterError("Please enter valid customer name", true);
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
    } else {
        fieldSenseTosterError("Location identifier can not be empty", true);
        return false;
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
        if ((isNaN(phone1))) {
            fieldSenseTosterError("Please enter valid phone number", true);
            return false;
        }
    }
    if (phone2.trim().length != 0) {
        if ((isNaN(phone2))) {
            fieldSenseTosterError("Please enter valid phone number", true);
            return false;
        }
    }
    if (phone3.trim().length != 0) {
        if ((isNaN(phone3))) {
            fieldSenseTosterError("Please enter valid phone number", true);
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
        if (email3.trim().length > 100) {
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
    if (territory.trim().length != 0) {
        if (territory.trim().length > 100) {
            fieldSenseTosterError("Territory can not be more than 100 characters", true);
            return false;
        }
    }
    if (industry.trim().length != 0) {
        if (industry.trim().length > 100) {
            fieldSenseTosterError("Industry can not be more than 100 characters", true);
            return false;
        }
    }
    $('#pleaseWaitDialog').modal('show');
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
        "customerWebsiteUrl2": website2,
        "modified_by": {
            "id": loginUserId
        }
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
                $('#Contacts').css("display", "block");
                $('#Notes').css("display", "block");
                $('#Activity').css("display", "block");
                //                $('.editCust').html('<button type="submit" class="btn btn-info" tabindex="17" onClick="editCustomer(' + customerId + ')">Save</button>');
                $('.custId').html('<button type="button" class="btn btn-info" tabindex="17" onclick="createContact(\'' + customerId + '\',\'' + customerPrintAs + '\');return false;">Save</button>');
                $('#id_notesSave').html('<button type="button" class="btn btn-info" tabindex="17" onClick="createNotes(' + customerId + ')">Save</button>');
                $('#id_addActivityTemplate').html('<button type="button" class="btn btn-sm btn-info" data-toggle="modal" href="#responsive4" onclick="addActivityInCustomerTemplate(\'' + customerId + '\',\'' + customerName + '\')"><i class="fa fa-plus"></i> Add New</button>');
                fieldSenseTosterSuccess("Customer saved successfully, please add contact(s)", true);
                $('#pleaseWaitDialog').modal('hide');
                //                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}




function clearIds() {
    $('#customertmt').html('');
    $('.editCust').html('<button type="submit" class="btn btn-info" onClick="createCustomer()">Save</button>');
    /// showCustomers();
    $('#id_firstname').val('');
    $('#id_middlename').val('');
    $('#id_lastname').val('');
    $('.cls_phonec1').val('');
    $('.cls_phonec2').val('');
    $('.cls_phonec3').val('');
    $('.cls_faxc1').val('');
    $('.cls_faxc2').val('');
    $('.cls_mobile1').val('');
    $('.cls_mobile2').val('');
    $('.cls_emailc1').val('');
    $('.cls_emailc2').val('');
    $('#id_reportsTo').val('');
    $('#id_assistancenm').val('');
    $('#id_spousenm').val('');
    $('#id_birthdate').val('');
    $('#id_anniversarydate').val('');
    $('#id_designation').val('');
}


function editCustomerDetails(customerId, customerPrintas) {
    $('#pleaseWaitDialog').modal('show');
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
            $('#id_leftMenu').html('');
            var customerData = data.result;
            if (data.errorCode == '0000') {
                var customerShowTemplateHtml = '';
                var customerId = customerData.id;
                var customerName = customerData.customerName;
                var customerPrintAs = customerData.customerPrintas;
                editCustomerPageTemplate(customerId, customerName);
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
                var latitude = customerData.lasknownLatitude;
                var longitude = customerData.lasknownLangitude;
                customerShowTemplateHtml += '<form class="form-horizontal" role="form">';
                customerShowTemplateHtml += '<div class="form-body">';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Customer Name</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="name" class="form-control" placeholder="Enter Customer Name" tabindex="1" value="' + customerName + '" id="id_customerNames">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Territory</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                //customerShowTemplateHtml += ' <input type="name" class="form-control" placeholder="Enter Territory" tabindex="1" id="id_territorys" value="' + territory + '">';
                // customerShowTemplateHtml += '<select class="form-control" tabindex="2" id="id_territorys"> </select>'; //dropdown instead of textbox
                customerShowTemplateHtml += '<button id="drilldown1" class="btn drilldown form-control" data-toggle="dropdown"><span class="text" placeholder="Select">Test</span></button>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Customer Print AS</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="name" class="form-control" placeholder="Enter Customer Name" tabindex="1" value="' + customerPrintAs + '" id="id_customerPrintAss">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Is Head Office</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<div class="checkbox-list">';
                if (isHeadOffice == true) {
                    customerShowTemplateHtml += '<input type="checkbox" checked id="id_isHeadOffices">';
                } else {
                    customerShowTemplateHtml += '<input type="checkbox" id="id_isHeadOffices">';
                }
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Location Identifier</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="location" class="form-control" placeholder="Enter Office Location" tabindex="3" value="' + customerLocationIdentifier + '" id="id_customerLocations">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Industry</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                //customerShowTemplateHtml += ' <input type="name" class="form-control" placeholder="Enter Industry" tabindex="1" id="id_industrys" value="' + industry + '">';
                customerShowTemplateHtml += '<select class="form-control" tabindex="2" id="id_industrys"> </select>'; //dropdown instead of textbox
                //customerShowTemplateHtml += '<button id="drilldown1" class="btn btn-primay btn-block drilldown" data-toggle="dropdown"><span class="text" placeholder="Select...">Test</span></button>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-12">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-2 control-label">Address</label>';
                customerShowTemplateHtml += '<div class="col-md-10">';
                customerShowTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter Address" tabindex="5" value="' + address + '" id="id_address">' + address + '</textarea>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-2">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-12 control-label">Office Building</label>';
                customerShowTemplateHtml += '<span class="help-block">(Pin the building in the adjoining map for accurate results)</span>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="col-md-10">';
                customerShowTemplateHtml += '<div id="gmap_marker_customer" class="gmaps" tabindex="6"></div>';
                customerShowTemplateHtml += '<span class="help-block">(Right-click to drop a pin on the map)</span>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Latitude, Longitude</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<div class="input-icon right">';
                customerShowTemplateHtml += '<i class="fa fa-map-marker"></i>';
                customerShowTemplateHtml += '<input type="text" class="form-control" placeholder="Enter Latitude, Longitude" tabindex="7" value="' + latitude + ',' + longitude + '" id="id_cuslatlng" disabled>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Phone1</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Phone1" class="form-control cls_phones1" placeholder="Enter Phone1" id="mask_phone" value="' + phone1 + '" tabindex="12">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Phone2</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Phone2" class="form-control cls_phones2" placeholder="Enter Phone2" id="mask_phone1" value="' + phone2 + '" tabindex="13">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Phone3</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Phone2" class="form-control cls_phones3" placeholder="Enter Phone2" id="mask_phone1" value="' + phone3 + '" tabindex="13">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Fax1</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Fax" class="form-control" placeholder="Enter Fax" value="' + fax1 + '" tabindex="14" id="id_faxs1">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Fax2</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Fax" class="form-control" placeholder="Enter Fax" value="' + fax2 + '" tabindex="14" id="id_faxs2">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Email1</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Email" class="form-control cls_emails1" placeholder="Enter Email" value="' + email1 + '" id="inputEmail1" tabindex="15">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Email2</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Email" class="form-control cls_emails2" placeholder="Enter Email" value="' + email2 + '" id="inputEmail1" tabindex="15">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Email3</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Email" class="form-control cls_emails3" placeholder="Enter Email" value="' + email3 + '" id="inputEmail1" tabindex="15">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Website1</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Website" class="form-control" placeholder="Enter Website" value="' + website1 + '" tabindex="16" id="id_websites1">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="row">';
                customerShowTemplateHtml += '<div class="col-md-6">';
                customerShowTemplateHtml += '<div class="form-group">';
                customerShowTemplateHtml += '<label class="col-md-4 control-label">Website2</label>';
                customerShowTemplateHtml += '<div class="col-md-8">';
                customerShowTemplateHtml += '<input type="Website" class="form-control" placeholder="Enter Website" value="' + website2 + '" tabindex="16" id="id_websites2">';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '<div class="buttons">';
                customerShowTemplateHtml += '<button type="submit" class="btn btn-info" tabindex="17" onClick="editCustomerOnEdit(' + customerId + ');return false;">Save</button>';
                customerShowTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default cls_btnspace" tabindex="18" onclick="window.location=\'customers.html\';">Cancel</button>';
                customerShowTemplateHtml += '</div>';
                customerShowTemplateHtml += '</form>';
                $('#id_editCustomer').append(customerShowTemplateHtml);
                $('#pleaseWaitDialog').modal('hide');
                $('.custId').html('<button type="submit" class="btn btn-info" onClick="createContact(\'' + customerId + '\',\'' + customerPrintas + '\');return false;">Save</button>');
                $('#id_notesSaveInEdit').html('<button class="btn btn-info" onClick="createNotes(' + customerId + ')" tabindex="17" type="button">Save</button>');
                loadIndustryTerritoryOnEdit(industry, territory);

                geoCodeMapForCustomer();
                App.init();
            }
        }
    });

}

function loadIndustryTerritoryOnEdit(industry, territory) {

    var countSelect = 0;
    var indTemplate = '';
    var terrTemplate = '';
    for (var i = 0; i < activeIndustryData.length; i++) {
        if (activeIndustryData[i].categoryName.trim() === industry.trim()) {
            indTemplate += "<option selected>" + activeIndustryData[i].categoryName + "</option>";
            countSelect++;
        } else {
            indTemplate += "<option >" + activeIndustryData[i].categoryName + "</option>";
        }
    }
    if (countSelect === 0) {
        indTemplate += "<option selected>" + industry + "</option>";
    }
    $("#id_industrys").html(indTemplate);
    /*  countSelect=0; 
     
     for (var i = 0; i < activeTerritoryData.length; i++) {
     if(activeTerritoryData[i].categoryName.trim()===territory.trim()){
     terrTemplate+= "<option selected>"+activeTerritoryData[i].categoryName+"</option>";
     countSelect++;
     }else{
     terrTemplate+= "<option >"+activeTerritoryData[i].categoryName+"</option>";
     }
     }
     if(countSelect===0){
     terrTemplate+= "<option selected>"+territory+"</option>";
     }*/
    // $("#id_territorys").html(terrTemplate);
    $('#drilldown1').drilldownSelect({appendValue: false, data: parentTerritoryCatagories});
    $("#drilldown1 > span").html('<span>' + territory.trim() + '</span>');
}

/*
 function loadIndustryTerritoryOnAdd() {
 
 var templateString='';
 if(activeIndustryData != undefined) {
 for (var i = 0; i < activeIndustryData.length; i++) {
 templateString+= "<option >"+activeIndustryData[i].categoryName+"</option>";
 }
 }
 $("#id_industry").html(templateString);
 templateString='';
 if(activeTerritoryData != undefined) {
 for (var i = 0; i < activeTerritoryData.length; i++) {
 templateString+= "<option >"+activeTerritoryData[i].categoryName+"</option>";
 }
 }
 
 $("#id_territory").html(templateString);
 }
 */
function contactDetails(customerId, customerPrintas) {
    $('#pleaseWaitDialog').modal('show');
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
        success: function (contactDatas) {
            $('#id_viewContactTemplate').html('');
            var contactData = contactDatas.result;
            if (contactDatas.errorCode == '0000') {
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
                    contactShowTemplateHtml += '<td>' + firstName + ' ' + middleName + ' ' + lastName + '</td>';
                    contactShowTemplateHtml += '<td>' + mobile1 + '</td>';
                    contactShowTemplateHtml += '<td>' + email1 + '</td>';
                    contactShowTemplateHtml += '<td>';
                    contactShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" onclick="editContactTemplate(\'' + customerContactId + '\',\'' + customerId + '\')" href="#responsive2" data-placement="bottom" title="Edit"><i class="fa fa-edit"></i></button>';
                    contactShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteCustomerContact(\'' + customerContactId + '\',\'' + fullName + '\',\'' + customerId + '\')"><i class="fa fa-trash-o"></i></button>';
                    contactShowTemplateHtml += '</td>';
                    contactShowTemplateHtml += '</tr>';
                }
                $('#id_viewContactTemplate').append(contactShowTemplateHtml);
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}
function notesDetails(customerId, customerPrintas) {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/notes/" + customerId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (notedatas) {
            if (notedatas.errorCode == '0000') {
                var noteTemplate = '';
                var notesData = notedatas.result;
                for (var i = 0; i < notesData.length; i++) {
                    var id = notesData[i].id;
                    var note = notesData[i].description;
                    var firstnm = notesData[i].userId.firstName;
                    var lastnm = notesData[i].userId.lastName;
                    var createdOn = notesData[i].createdOn;
                    createdOn = fieldSenseDateTimeForNotes(createdOn);
                    var fullNm = firstnm + ' ' + lastnm;
                    var id_comments = 'comments' + id;
                    noteTemplate += '<div class="form-group comm">';
                    noteTemplate += '<div class="col-md-12">';
                    noteTemplate += '<label id="' + id_comments + '">' + note + '</label>';
                    noteTemplate += '</div>';
                    noteTemplate += '<label class="col-md-12"> <span class="time-note">Added by ' + fullNm + ' on ' + createdOn;
                    noteTemplate += '<button type="button" class="btn btn-xs btn-default" data-toggle="modal" href="#responsive5" onclick="editNotesTemplate(\'' + customerId + '\',\'' + id + '\')"><i class="fa fa-edit"></i></button></label>';
                    noteTemplate += '</div>';
                }
                $('.cls_viewNotesOneCustomer').html(noteTemplate);
                $('#pleaseWaitDialog').modal('hide');
            } else {
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}
function appoinmentDetails(customerId, customerPrintas) {
    $('#pleaseWaitDialog').modal('show');
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
        success: function (appointmentData) {
            $('#id_toShowActivityInCustomer').html('');
            if (appointmentData.errorCode == '0000') {
                var activityData = appointmentData.result;
                var activityShowTemplate = "";
                if (activityData.length != 0) {
                    for (var i = 0; i < activityData.length; i++) {
                        var activity = activityData[i].title;
                        var description = activityData[i].description;
                        var purpose = activityData[i].purpose.purpose;
                        if (activity == null || activity == "")
                        {
                            activity = purpose;
                        }
                        var activityOutCome = activityData[i].outcome; // Modified by jyoti
                        var appointmentId = activityData[i].id;
                        var assigneeName = activityData[i].assignedTo.fullname; // Added by jyoti
                        var activityDateTime = activityData[i].sdateTime
                        activityDateTime = activityDateTime.split(' ');
                        var activityDate = activityDateTime[0];
                        activityDate = activityDate.split('-');
                        var activityTime = activityDateTime[1];
                        activityTime = activityTime.split(':');
                        var activityDateJs = convertServerDateToLocalDate(activityDate[0], activityDate[1] - 1, activityDate[2], activityTime[0], activityTime[1]);
                        activityDateTime = activityDateJs.getFullYear() + "-" + (activityDateJs.getMonth() + 1) + "-" + activityDateJs.getDate() + " " + activityDateJs.getHours() + ":" + activityDateJs.getMinutes() + ":00.0";
                        activityDateTime = fieldSenseseForJSDate(activityDateTime);
                        var activityStatusClass = '';
                        if (activityOutCome == 0) {
                            activityStatusClass = 'form-group basic'; // "basic" Added by jyoti
                        } else if (activityOutCome == 1) {
                            activityStatusClass = 'form-group success';
                        }
                        else if (activityOutCome == 2) {
                            activityStatusClass = 'form-group failed';
                        }

                        // Added by jyoti, purpose - to display the name of visitors with some style change 
                        // Added by jyoti, to get today's date. compare dates if equal, display only time 
                        var date = Date(); // don't use new keyword
                        var mm = date.substring(4, 7);
                        var dd = date.substring(8, 10);
                        var yy = date.substring(11, 15);
                        var todayDate = dd + " " + mm + " " + yy;
                        var getActivityDate = activityDateTime.split(',');
                        var today_date = new Date(todayDate);
                        var activity_date = new Date(getActivityDate[0]);
                        var displayActivityDateTime;
                        if (today_date.getTime() == activity_date.getTime()) {
                            displayActivityDateTime = getActivityDate[1];
                        } else {
                            displayActivityDateTime = activityDateTime;
                        }
                        activityShowTemplate += '<div class="form-body">';
                        activityShowTemplate += '<div class="' + activityStatusClass + '">';
                        activityShowTemplate += '<button type="button" style="color:#0d638f" class="btn btn-link btn-sm"  data-placement="bottom" data-toggle="modal" href="#responsive4" title="Edit" onclick="editVisitTemplate(\'' + appointmentId + '\',\'' + customerId + '\',\'' + customerPrintas + '\');return false;">' + activity + '</button>';
                        activityShowTemplate += '<label class="col-md-12 dsc-label">' + description + '</label>';
                        activityShowTemplate += '<label class="col-md-12 time-note">by ' + assigneeName + ' @ ' + displayActivityDateTime + '</label>';
                        activityShowTemplate += '</div>';
                        activityShowTemplate += '</div>';
                        // Ended by jyoti
                        // Commented by jyoti
//                        activityShowTemplate += '<div class="form-body">';
//                        activityShowTemplate += '<div class="' + activityStatusClass + '">';
//                        activityShowTemplate += '<label class="col-md-2 time-label">' + activityDateTime + '</label>';
//                        activityShowTemplate += '<div class="col-md-1">';
//                        activityShowTemplate +='<button type="button" class="btn btn-default btn-xs tooltips"  data-placement="bottom" data-toggle="modal" href="#responsive4" title="Edit" onclick="editVisitTemplate(\'' + appointmentId + '\',\'' + customerId + '\',\'' + customerPrintas + '\');return false;"><i class="fa fa-edit"></i></button>';
//                        activityShowTemplate += '</div>';
//                        activityShowTemplate += '<label class="col-md-12 title-label">' + activity + '</label>';
//                        activityShowTemplate += '<label class="col-md-12 dsc-label">' + description + '</label>';
//                        activityShowTemplate += '</div>';
//                        activityShowTemplate += '</div>';
                    }
                }
            }
            $('#id_toShowActivityInCustomer').html(activityShowTemplate);
            $('#pleaseWaitDialog').modal('hide');
        }
    });
}
function editVisitTemplate(appointmentId, customerId, customerName) {
    $('.cls_addActivityInCustomer').html("");
    $('#pleaseWaitDialog').modal('show');
    var count = 0;
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/appointment/" + appointmentId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (dataActivity) {
            if (dataActivity.errorCode == '0000') {



                $.ajax({
                    type: "GET",
                    url: fieldSenseURL + "/activityPurpose",
                    contentType: "application/json; charset=utf-8",
                    headers: {
                        "userToken": userToken
                    },
                    crossDomain: false,
                    cache: false,
                    dataType: 'json',
                    asyn: true,
                    success: function (data) {
                        if (data.errorCode == '0000') {
                            var purposeData = data.result;
                            $.ajax({
                                type: "GET",
                                url: fieldSenseURL + "/team/userPositions/" + loginUserId,
                                contentType: "application/json; charset=utf-8",
                                headers: {
                                    "userToken": userToken
                                },
                                crossDomain: false,
                                cache: false,
                                dataType: 'json',
                                asyn: true,
                                success: function (datateamMember) {
                                    if (datateamMember.errorCode == '0000') {
                                        var teamMemberData = datateamMember.result;
                                        $.ajax({
                                            type: "GET",
                                            url: fieldSenseURL + "/activityOutcome",
                                            contentType: "application/json; charset=utf-8",
                                            headers: {
                                                "userToken": userToken
                                            },
                                            crossDomain: false,
                                            cache: false,
                                            dataType: 'json',
                                            asyn: true,
                                            success: function (dataOutCome) {
                                                if (dataOutCome.errorCode == '0000') {
                                                    var activityData = dataActivity.result;
                                                    var customerId = activityData.customer.id;
                                                    var ownerId = activityData.owner.id;
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
                                                        success: function (dataCustomerContact) {
                                                            if (dataCustomerContact.errorCode == '0000') {
                                                                var dataCustomerContact = dataCustomerContact.result;
                                                                $.ajax({
                                                                    type: "GET",
                                                                    url: fieldSenseURL + "/team/TL",
                                                                    contentType: "application/json; charset=utf-8",
                                                                    headers: {
                                                                        "userToken": userToken
                                                                    },
                                                                    crossDomain: false,
                                                                    cache: false,
                                                                    dataType: 'json',
                                                                    asyn: true,
                                                                    success: function (datateamLead) {
                                                                        if (datateamLead.errorCode == '0000') {
                                                                            var datateamLead = datateamLead.result;
                                                                            $.ajax({
                                                                                type: "GET",
                                                                                url: fieldSenseURL + "/team/userPositions/" + ownerId,
                                                                                contentType: "application/json; charset=utf-8",
                                                                                headers: {
                                                                                    "userToken": userToken
                                                                                },
                                                                                crossDomain: false,
                                                                                cache: false,
                                                                                dataType: 'json',
                                                                                asyn: true,
                                                                                success: function (datateamMember) {
                                                                                    if (datateamMember.errorCode == '0000') {
                                                                                        datateamMember = datateamMember.result;
                                                                                        var activityTitle = activityData.title;
                                                                                        var activityStatus = activityData.status;
                                                                                        var activityDescription = activityData.description;
                                                                                        var contactId = activityData.customerContact.id;
                                                                                        var customerPrint = activityData.customer.customerPrintas;
                                                                                        var customerName = activityData.customer.customerName;
                                                                                        var dateTime = activityData.sdateTime;
                                                                                        var endDateTime = activityData.sendTime;
                                                                                        var purposeId = activityData.purpose.id;
                                                                                        var purpose = activityData.purpose.purpose;

                                                                                        var ownerName = activityData.owner.firstName + ' ' + activityData.owner.lastName;
                                                                                        var outcomeId = activityData.outcomes.id;
                                                                                        var assignId = activityData.assignedTo.id;
                                                                                        var assigneeName = activityData.assignedTo.firstName + ' ' + activityData.assignedTo.lastName
                                                                                        dateTime = dateTime.split(' ');
                                                                                        var starttDate = dateTime[0];
                                                                                        var splitStartDate = starttDate.split('-');
                                                                                        var startTime = dateTime[1];
                                                                                        var splitStartTime = startTime.split(':');
                                                                                        var startDateJs = convertServerDateToLocalDate(splitStartDate[0], splitStartDate[1] - 1, splitStartDate[2], splitStartTime[0], splitStartTime[1]);
                                                                                        var startDate = (startDateJs.getDate() < 10) ? '0' + startDateJs.getDate() : startDateJs.getDate();
                                                                                        var startMonth = (startDateJs.getMonth() < 9) ? '0' + (startDateJs.getMonth() + 1) : (startDateJs.getMonth() + 1);
                                                                                        starttDate = startDate + '-' + startMonth + '-' + startDateJs.getFullYear();
                                                                                        var startHr = startDateJs.getHours();
                                                                                        var startMin = startDateJs.getMinutes();
                                                                                        endDateTime = endDateTime.split(' ');
                                                                                        var endDate = endDateTime[0];
                                                                                        var splitEndDate = endDate.split('-');
                                                                                        var endTime = endDateTime[1];
                                                                                        var splitEndTime = endTime.split(':');
                                                                                        var endDateJs = convertServerDateToLocalDate(splitEndDate[0], splitEndDate[1] - 1, splitEndDate[2], splitEndTime[0], splitEndTime[1]);
                                                                                        var endHr = endDateJs.getHours();
                                                                                        var endMin = endDateJs.getMinutes();
                                                                                        var outData = dataOutCome.result;
                                                                                        var outcomeDescription = activityData.outcomeDescription;
                                                                                        var editActivityTemplateHtml = '';
                                                                                        editActivityTemplateHtml += '<div class="modal-dialog modal-wide">';
                                                                                        editActivityTemplateHtml += '<div class="modal-content">';
                                                                                        editActivityTemplateHtml += '<div class="modal-header">';
                                                                                        editActivityTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                                                                                        editActivityTemplateHtml += '<h4 class="modal-title">Edit Visit</h4>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<form action="#" class="form-horizontal form-row-seperated">';
                                                                                        editActivityTemplateHtml += '<div class="modal-body">';
                                                                                        editActivityTemplateHtml += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" id="id_editactivity">';
                                                                                        editActivityTemplateHtml += '<div class="form-body">';
                                                                                        editActivityTemplateHtml += '<div class="row">';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Customer Name<span style="color:red"> *</span></label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-7">';
                                                                                        editActivityTemplateHtml += '<input type="hidden" id="id_hiddenEdit" value="' + customerId + '"/>';
                                                                                        editActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control" id="autocompleteCnm" placeholder="Enter text" value="' + customerName + '" disabled>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Contact<span style="color:red"> *</span></label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-7">';
                                                                                        editActivityTemplateHtml += '<select class="form-control" id="id_contactEdit">';

                                                                                        editActivityTemplateHtml += '<option>None</option>';
                                                                                        for (var i = 0; i < dataCustomerContact.length; i++) {
                                                                                            var contId = dataCustomerContact[i].id;
                                                                                            var firstName = dataCustomerContact[i].firstName;
                                                                                            var middleName = dataCustomerContact[i].middleName;
                                                                                            var lastName = dataCustomerContact[i].lastName;
                                                                                            var fullName = firstName + ' ' + middleName + ' ' + lastName;
                                                                                            if (contactId == contId) {
                                                                                                editActivityTemplateHtml += '<option value="' + contId + '" selected>' + fullName + '</option>';
                                                                                            } else {
                                                                                                editActivityTemplateHtml += '<option value="' + contId + '">' + fullName + '</option>';
                                                                                            }
                                                                                        }
                                                                                        editActivityTemplateHtml += '</select>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="row">';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Title</label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-7">';
                                                                                        editActivityTemplateHtml += '<input type="activity" class="form-control" placeholder="Enter text" id="id_activityEdit" value="' + activityTitle + '">';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Date<span style="color:red"> *</span></label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-7">';
                                                                                        editActivityTemplateHtml += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
                                                                                        editActivityTemplateHtml += '<input type="text" class="form-control" id="id_dateEdit" readonly value="' + starttDate + '">';
                                                                                        editActivityTemplateHtml += '<span class="input-group-btn">';
                                                                                        editActivityTemplateHtml += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                                                                                        editActivityTemplateHtml += '</span>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="row">';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Start Time<span style="color:red"> *</span></label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-3">';
                                                                                        editActivityTemplateHtml += '<select id="edit_Shr" class="form-control input-xssmall">';
                                                                                        editActivityTemplateHtml += '<option value="HH">HH</option>';
                                                                                        for (var j = 0; j < 24; j++) {
                                                                                            if (j < 10)
                                                                                            {
                                                                                                if (startHr == j) {
                                                                                                    editActivityTemplateHtml += '<option value="' + j + '" selected>0' + j + '</option>';
                                                                                                }
                                                                                                else {
                                                                                                    editActivityTemplateHtml += '<option value="' + j + '">0' + j + '</option>';
                                                                                                }
                                                                                            } else {
                                                                                                if (startHr == j) {
                                                                                                    editActivityTemplateHtml += '<option value="' + j + '" selected>' + j + '</option>';
                                                                                                }
                                                                                                else {
                                                                                                    editActivityTemplateHtml += '<option value="' + j + '">' + j + '</option>';
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        editActivityTemplateHtml += '</select></div>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-3">';
                                                                                        editActivityTemplateHtml += '<select id="edit_Smin" class="form-control input-xssmall">';
                                                                                        editActivityTemplateHtml += '<option value="MM">MM</option>';
                                                                                        for (var k = 0; k < 60; k = k + 1) {
                                                                                            if (k < 10)
                                                                                            {
                                                                                                if (startMin == k) {
                                                                                                    editActivityTemplateHtml += '<option value="' + k + '" selected>0' + k + '</option>';
                                                                                                }
                                                                                                else {
                                                                                                    editActivityTemplateHtml += '<option value="' + k + '">0' + k + '</option>';
                                                                                                }
                                                                                            } else {
                                                                                                if (startMin == k) {
                                                                                                    editActivityTemplateHtml += '<option value="' + k + '" selected>' + k + '</option>';
                                                                                                } else {
                                                                                                    editActivityTemplateHtml += '<option value="' + k + '">' + k + '</option>';
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        editActivityTemplateHtml += '</select>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">End Time<span style="color:red"> *</span></label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-3">';
                                                                                        editActivityTemplateHtml += '<select id="edit_ehr" class="form-control input-xssmall">';
                                                                                        editActivityTemplateHtml += '<option value="HH">HH</option>';
                                                                                        for (var l = 0; l < 24; l++) {
                                                                                            if (l < 10)
                                                                                            {
                                                                                                if (endHr == l) {
                                                                                                    editActivityTemplateHtml += '<option value="' + l + '" selected>0' + l + '</option>';
                                                                                                }
                                                                                                else {
                                                                                                    editActivityTemplateHtml += '<option value="' + l + '">0' + l + '</option>';
                                                                                                }
                                                                                            }
                                                                                            else {
                                                                                                if (endHr == l) {
                                                                                                    editActivityTemplateHtml += '<option value="' + l + '" selected>' + l + '</option>';
                                                                                                } else {
                                                                                                    editActivityTemplateHtml += '<option value="' + l + '">' + l + '</option>';
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        editActivityTemplateHtml += '</select></div>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-3">';
                                                                                        editActivityTemplateHtml += '<select id="edit_emin" class="form-control input-xssmall">';
                                                                                        editActivityTemplateHtml += '<option value="MM">MM</option>';
                                                                                        for (var m = 0; m < 60; m = m + 1) {
                                                                                            if (m < 10)
                                                                                            {
                                                                                                if (endMin == m) {
                                                                                                    editActivityTemplateHtml += '<option value="' + m + '" selected>0' + m + '</option>';
                                                                                                } else {
                                                                                                    editActivityTemplateHtml += '<option value="' + m + '">0' + m + '</option>';
                                                                                                }
                                                                                            } else {
                                                                                                if (endMin == m) {
                                                                                                    editActivityTemplateHtml += '<option value="' + m + '" selected>' + m + '</option>';
                                                                                                } else {
                                                                                                    editActivityTemplateHtml += '<option value="' + m + '">' + m + '</option>';
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        editActivityTemplateHtml += '</select>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="row">';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Purpose</label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-7">';
                                                                                        editActivityTemplateHtml += '<select class="form-control" id="id_purposeEdit">';
                                                                                        for (var i = 0; i < purposeData.length; i++) {
                                                                                            var id = purposeData[i].id;
                                                                                            var purpose = purposeData[i].purpose;
                                                                                            if (id == purposeId) {
                                                                                                editActivityTemplateHtml += '<option value="' + id + '" selected>' + purpose + '</option>';
                                                                                            } else {
                                                                                                editActivityTemplateHtml += '<option value="' + id + '">' + purpose + '</option>';
                                                                                            }
                                                                                        }
                                                                                        editActivityTemplateHtml += '</select>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="row">';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Owner</label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-7">';
                                                                                        //
                                                                                        // activityTemplate += '<div class="col-md-7">';
                                                                                        if (role == 1 || role == 3 || role == 6 || role == 8) {
                                                                                            editActivityTemplateHtml += '<select class="form-control" id="id_owner" onchange="assignedToListEdit(this.value)" disabled>';
                                                                                            editActivityTemplateHtml += '<option>None</option>';
                                                                                            for (var i = 0; i < datateamLead.length; i++) {
                                                                                                var userId = datateamLead[i].user.id;
                                                                                                var firstName = datateamLead[i].user.firstName;
                                                                                                var lastName = datateamLead[i].user.lastName;
                                                                                                var fullName = firstName + ' ' + lastName;
                                                                                                editActivityTemplateHtml += '<option value="' + userId + '">' + fullName + '</option>';
                                                                                            }
                                                                                        } else {
                                                                                            editActivityTemplateHtml += '<select class="form-control" id="id_owner" onchange="assignedToListEdit(this.value)" disabled>';
                                                                                        }
                                                                                        editActivityTemplateHtml += '</select>';
                                                                                        // activityTemplate += '</div>';


                                                                                        //
                                                                                        //editActivityTemplateHtml += '<input type="Cname" name="currency" class="form-control cls_ownerEdit" id="' + ownerId + '" value="' + ownerName + '">';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Assigned To</label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-7">';
                                                                                        editActivityTemplateHtml += '<select class="form-control" id="id_assigned">';
                                                                                        //editActivityTemplateHtml += '<option>None</option>';

                                                                                        if (count == 0)
                                                                                        {
                                                                                            var id = $('#id_owner').val();
                                                                                            var value = $("#id_owner option:selected").text();
                                                                                            editActivityTemplateHtml += '<option value="' + assignId + '">' + assigneeName + '</option>';
                                                                                            //editActivityTemplateHtml += '<option value="' + ownerId + '">' + value + '</option>';
                                                                                            var length = datateamMember.length;
                                                                                            for (var i = 0; i < length; i++) {
                                                                                                var userId = datateamMember[i].user.id;
                                                                                                if (userId != assignId) {
                                                                                                    var firstName = datateamMember[i].user.firstName;
                                                                                                    var lastName = datateamMember[i].user.lastName;
                                                                                                    var fullName = firstName + ' ' + lastName;
                                                                                                    editActivityTemplateHtml += '<option value="' + userId + '">' + fullName + '</option>';
                                                                                                }
                                                                                            }
                                                                                            count++;
                                                                                        }





                                                                                        editActivityTemplateHtml += '</select>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="row">';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Status</label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-7">';
                                                                                        editActivityTemplateHtml += '<select class="form-control" id="id_statusEdit" onchange="outcomeForAddActivity()">';
                                                                                        editActivityTemplateHtml += '<option>Select</option>';
                                                                                        if (activityStatus == 0) {
                                                                                            editActivityTemplateHtml += '<option value="2">Completed</option>';
                                                                                            editActivityTemplateHtml += '<option value="0" selected>Pending</option>';
                                                                                            editActivityTemplateHtml += '<option value="1">In-Progress</option>';
                                                                                            editActivityTemplateHtml += '<option value="3">Cancelled</option>';
                                                                                        }
                                                                                        if (activityStatus == 1) {
                                                                                            editActivityTemplateHtml += '<option value="2">Completed</option>';
                                                                                            editActivityTemplateHtml += '<option value="0">Pending</option>';
                                                                                            editActivityTemplateHtml += '<option value="1" selected>In-Progress</option>';
                                                                                            editActivityTemplateHtml += '<option value="3">Cancelled</option>';
                                                                                        }
                                                                                        if (activityStatus == 2) {
                                                                                            editActivityTemplateHtml += '<option value="2" selected>Completed</option>';
                                                                                            editActivityTemplateHtml += '<option value="0">Pending</option>';
                                                                                            editActivityTemplateHtml += '<option value="1">In-Progress</option>';
                                                                                            editActivityTemplateHtml += '<option value="3">Cancelled</option>';
                                                                                        }
                                                                                        if (activityStatus == 3) {
                                                                                            editActivityTemplateHtml += '<option value="2">Completed</option>';
                                                                                            editActivityTemplateHtml += '<option value="0">Pending</option>';
                                                                                            editActivityTemplateHtml += '<option value="1">In-Progress</option>';
                                                                                            editActivityTemplateHtml += '<option value="3" selected>Cancelled</option>';
                                                                                        }
                                                                                        editActivityTemplateHtml += '</select>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-6 form-group">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-5 control-label">Outcome</label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-7">';
                                                                                        editActivityTemplateHtml += '<select class="form-control" id="id_outcomeEdit">';
                                                                                        for (var i = 0; i < outData.length; i++) {
                                                                                            var outId = outData[i].id;
                                                                                            var out = outData[i].outcome;
                                                                                            if (outId == outcomeId) {
                                                                                                editActivityTemplateHtml += '<option value="' + outId + '" selected>' + out + '</option>';
                                                                                            }
                                                                                            else {
                                                                                                editActivityTemplateHtml += '<option value="' + outId + '">' + out + '</option>';
                                                                                            }
                                                                                        }
                                                                                        editActivityTemplateHtml += '</select>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="row">';
                                                                                        editActivityTemplateHtml += '<div class="form-group" id="dscp">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-3 control-label">Description</label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-9">';
                                                                                        editActivityTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_descriptionEdit">' + activityDescription + '</textarea>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';

                                                                                        editActivityTemplateHtml += '<span id="id_outcomedescriptiontextbox" style="display:block;">';
                                                                                        editActivityTemplateHtml += '<div class="row">';
                                                                                        editActivityTemplateHtml += '<div class="col-md-12 form-group" id="dscp">';
                                                                                        editActivityTemplateHtml += '<label class="col-md-3 control-label">Outcome Description</label>';
                                                                                        editActivityTemplateHtml += '<div class="col-md-9">';
                                                                                        editActivityTemplateHtml += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_outcomedescription">' + outcomeDescription + '</textarea>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</span>';

                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '<div class="modal-footer">';

                                                                                        editActivityTemplateHtml += '<button type="submit" class="btn btn-info" onclick="editActivity(\'' + appointmentId + '\',\'' + customerName + '\',\'' + customerId + '\');return false;">Save</button>';

                                                                                        editActivityTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</form>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        editActivityTemplateHtml += '</div>';
                                                                                        $('.cls_addActivityInCustomer').html(editActivityTemplateHtml);
                                                                                        $('#id_owner').val(ownerId);
                                                                                        //                                                                $('#autocompleteCnm').autocomplete({
//								    minChars:3,
//                                                                    //lookup: customer,
//								    paramName: 'searchText',
//    								    transformResult: function(response) {
//        								return {
//            									suggestions: $.map(response.result, function(dataItem) {
//                								return { value: dataItem.value, id:dataItem.customerId  };
//            									})
//        								};
//    								    },
//		        					    dataType:"json",
//								    serviceUrl: fieldSenseURL + '/customer/visit?userToken='+userToken,
//                                                                    onSelect: function (suggestion) {
//                                                                        $('#id_hiddenEdit').val(suggestion.id);
//                                                                        customerContacts(suggestion.id)
//                                                                    },
//                        					   /* onSearchStart: function (query) {
//								 		$('#pleaseWaitDialog').modal('show');
//	                					    },
//		        					    onSearchComplete: function (query, suggestions) {
//										$('#pleaseWaitDialog').modal('hide');
//		        					    }*/
//                                                                });
                                                                                        if (jQuery().datepicker) {
                                                                                            $('.date-picker').datepicker({
                                                                                                rtl: App.isRTL(),
                                                                                                autoclose: true
                                                                                            });
                                                                                            $('body').removeClass("modal-open"); // fix bug when inline picker is used in modal
                                                                                        }
                                                                                        $('#id_editactivity').slimScroll({
                                                                                            size: '7px',
                                                                                            color: '#bbbbbb',
                                                                                            opacity: .9,
                                                                                            position: false ? 'left' : 'right',
                                                                                            height: 420,
                                                                                            allowPageScroll: false,
                                                                                            disableFadeOut: false
                                                                                        });
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });

                                    }
                                },
                                error: function (xhr, ajaxOptions, thrownError) {
                                    $('#pleaseWaitDialog').modal('hide');
                                    alert("error in ajax call : " + thrownError);
                                    //toggleButton(el);
                                },
                            });
                        }
                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        $('#pleaseWaitDialog').modal('hide');
                        alert("error in ajax call : " + thrownError);
                        //toggleButton(el);
                    },
                });
            } else {
                fieldSenseTosterError(dataActivity.errorMessage, true);
                FieldSenseInvalidToken(dataActivity.errorMessage);
                $(".cls_addActivityInCustomer").modal('hide');
                if (dataActivity.errorMessage.indexOf("deleted") != -1) {
                    activityShowTemplate(teamId, user);
                }
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#pleaseWaitDialog').modal('hide');
            alert("error in ajax call : " + thrownError);
            //toggleButton(el);
        },
    });
    $('#pleaseWaitDialog').modal('hide');
}
function editActivity(appointmentId, customerName, customerId) {
    var customer = document.getElementById("id_hiddenEdit").value.trim();
    if (customer.trim().length == 0) {
        fieldSenseTosterError("Customer Name cannot be empty", true);
        return false;
    }
    var contact = document.getElementById("id_contactEdit").value.trim();
    if (contact.trim() == "" || contact.trim() == "None") {
        contact = 0;
    }
    var activity = document.getElementById("id_activityEdit").value.trim();
//    if (activity.trim().length == 0) {
//        fieldSenseTosterError("Activity cannot be empty", true);
//        return false;
//    }
    var dates = document.getElementById("id_dateEdit").value;
    if (dates.trim().length == 0) {
        fieldSenseTosterError("Date cannot be empty", true);
        return false;
    }
    var startHr = document.getElementById("edit_Shr").value.trim();
    if (startHr == 'HH') {
        fieldSenseTosterError("Start Time sould be selected", true);
        return false;
    }
    var startMin = document.getElementById("edit_Smin").value.trim();
    if (startMin == 'MM') {
        fieldSenseTosterError("Start Time sould be selected", true);
        return false;
    }
    var endHr = document.getElementById("edit_ehr").value.trim();
    if (endHr == "HH") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    var endMin = document.getElementById("edit_emin").value.trim();
    if (endMin == "MM") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    if (parseInt(startHr) > parseInt(endHr) || (startHr == endHr && parseInt(startMin) > parseInt(endMin))) {
        fieldSenseTosterError("Start Time should be less than End Time", true);
        return false;
    }
    var purpose = document.getElementById("id_purposeEdit").value.trim();
    if (purpose === 'Select') {
        fieldSenseTosterError("Purpose cannot be empty", true);
        return false;
    }
    //var owner = document.getElementsByClassName("cls_ownerEdit")[0].id.trim();
    var owner = document.getElementById("id_owner").value.trim();
    var assignedTo = document.getElementById("id_assigned").value;
    if (assignedTo === 'None') {
        fieldSenseTosterError("Please select proper assigned to", true);
        return false;
    }
    var status = document.getElementById("id_statusEdit").value;
    if (status === 'Select') {
        fieldSenseTosterError("Please select proper status", true);
        return false;
    }
    var outcome = document.getElementById("id_outcomeEdit").value;
    var description = document.getElementById("id_descriptionEdit").value.trim();
    if (description.length > 1) {
        if (description.length > 1000) {
            fieldSenseTosterError("Description can not be more than 1000 characters", true);
            return false;
        }
    }
    var outcomeDescription = document.getElementById("id_outcomedescription").value.trim();
    if (outcomeDescription.trim().length != 0) {
        if (outcomeDescription.trim().length > 2000) {
            fieldSenseTosterError("Outcome Description can not be more than 2000 character", true);
            return false;
        }
    }
    var dateSplit = dates.split("-");
    var startDate = convertLocalDateToServerDate(dateSplit[2], dateSplit[1] - 1, dateSplit[0], startHr, startMin);
    var adate = startDate.getFullYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDate() + " " + startDate.getHours() + ":" + startDate.getMinutes() + ':00';
    var endtHours = endHr + ":" + endMin;
    var endDTime;
    // if (endtHours.trim() == "HH:MM") {
    // endDTime = adate;
    // } else {
    endDTime = convertLocalDateToServerDate(dateSplit[2], dateSplit[1] - 1, dateSplit[0], endHr, endMin);
    endDTime = endDTime.getFullYear() + "-" + (endDTime.getMonth() + 1) + "-" + endDTime.getDate() + " " + endDTime.getHours() + ":" + endDTime.getMinutes() + ':00';
    //}
    var activityObject = {
        "id": appointmentId,
        "title": activity,
        "sdateTime": adate,
        "sendTime": endDTime,
        "customer": {
            "id": customer
        },
        "customerContact": {
            "id": contact
        },
        "owner": {
            "id": owner
        },
        "assignedTo": {
            "id": assignedTo
        },
        "description": description,
        "purpose": {
            "id": purpose
        },
        "outcomes": {
            "id": outcome
        },
        "status": status,
        "outcomeDescription": outcomeDescription,
        "updated_by": {
            "id": loginUserId
        }
    }
    $('#pleaseWaitDialog').modal('show');
    var jsonData = JSON.stringify(activityObject);
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + '/appointment/update',
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                // activityShowTemplate(teamId, userId);
                appoinmentDetails(customerId, customerName);
                $('#pleaseWaitDialog').modal('hide');
                $(".cls_addActivityInCustomer").modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}


function editCustomerOnEdit(customerId) {
    var customerName = document.getElementById("id_customerNames").value.trim();
    if (customerName.trim().length == 0) {
        fieldSenseTosterError("Customer Name cannot be empty", true);
        return false;
    }
    if (customerName.trim().length > 200) {
        fieldSenseTosterError("Customer name can not be more than 200 characters", true);
        return false;
    }
    var customerPrintAs = document.getElementById("id_customerPrintAss").value.trim();
    var customerLocationIdentifier = document.getElementById("id_customerLocations").value.trim();
    var isHeadOffice = document.getElementById("id_isHeadOffices").checked;
    // var territory = document.getElementById("id_territorys").value.trim();
    var territory = $("#drilldown1 > span").text().trim();
    if (territory === "Select") {
        fieldSenseTosterError("Select Territory Category ", true);
        return false;
    }
    territory = $("#drilldown1 > span > span").text().trim();
    var industry = document.getElementById("id_industrys").value.trim();
    var address = document.getElementById("id_address").value.trim();
    var custlatlong = document.getElementById("id_cuslatlng").value.trim();
    var phone1 = document.getElementsByClassName("cls_phones1")[0].value.trim();
    var phone2 = document.getElementsByClassName("cls_phones2")[0].value.trim();
    var phone3 = document.getElementsByClassName("cls_phones3")[0].value.trim();
    var fax1 = document.getElementById("id_faxs1").value.trim();
    var fax2 = document.getElementById("id_faxs2").value.trim();
    var email1 = document.getElementsByClassName("cls_emails1")[0].value.trim();
    var email2 = document.getElementsByClassName("cls_emails2")[0].value.trim();
    var email3 = document.getElementsByClassName("cls_emails3")[0].value.trim();
    var website1 = document.getElementById("id_websites1").value.trim();
    var website2 = document.getElementById("id_websites2").value.trim();

    var re = new RegExp("^[^!@#$%^&*~]");
    if (!(re.test(customerName))) {
        fieldSenseTosterError("Please enter valid customer name", true);
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
    } else {
        fieldSenseTosterError("Location identifier can not be empty", true);
        return false;
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
        if ((isNaN(phone1))) {
            fieldSenseTosterError("Please enter valid phone number", true);
            return false;
        }
    }
    if (phone2.trim().length != 0) {
        if ((isNaN(phone2))) {
            fieldSenseTosterError("Please enter valid phone number", true);
            return false;
        }
    }
    if (phone3.trim().length != 0) {
        if ((isNaN(phone3))) {
            fieldSenseTosterError("Please enter valid phone number", true);
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
    if (email2.trim().length != 0) {
        if (email2.trim().length > 100) {
            fieldSenseTosterError("Email2 can not be more than 100 characters", true);
            return false;
        }
    }
    if (email3.trim().length != 0) {
        if (email3.trim().length > 100) {
            fieldSenseTosterError("Email3 can not be more than 100 characters", true);
            return false;
        }
    }
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
    if (territory.trim().length != 0) {
        if (territory.trim().length > 100) {
            fieldSenseTosterError("Territory can not be more than 100 characters", true);
            return false;
        }
    }
    if (industry.trim().length != 0) {
        if (industry.trim().length > 100) {
            fieldSenseTosterError("ndustry can not be more than 100 characters", true);
            return false;
        }
    }
    custlatlong = custlatlong.split(',');
    var custLatitude = custlatlong[0];
    var custLangitude = custlatlong[1];
    customerName = htmlEntities(customerName);
    customerPrintAs = htmlEntities(customerPrintAs);
    customerLocationIdentifier = htmlEntities(customerLocationIdentifier);
    territory = htmlEntities(territory);
    industry = htmlEntities(industry);
    address = htmlEntities(address);
    $('#pleaseWaitDialog').modal('show');
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
        "customerWebsiteUrl2": website2,
        "lasknownLatitude": custLatitude,
        "lasknownLangitude": custLangitude,
        "modified_by": {
            "id": loginUserId
        }
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
            if (data.errorCode == '0000') {
                //added by nikhil
                clevertap.event.push("Edit Customer", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                //ended by nikhil
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}




function editContact(customerContactId, customerId) {
    if (doubleClickFlag) {
        var firstName = document.getElementById("id_fnm").value.trim();
        var middleName = document.getElementById("id_mnm").value.trim();
        var lastName = document.getElementById("id_lnm").value.trim();
        var phone1 = document.getElementsByClassName("cls_ph1")[0].value.trim();
        var phone2 = document.getElementsByClassName("cls_ph2")[0].value.trim();
        var phone3 = document.getElementsByClassName("cls_ph3")[0].value.trim();
        var fax1 = document.getElementsByClassName("cls_fx1")[0].value.trim();
        var fax2 = document.getElementsByClassName("cls_fx2")[0].value.trim();
        var mobile1 = document.getElementsByClassName("cls_mb1")[0].value.trim();
        var mobile2 = document.getElementsByClassName("cls_mb2")[0].value.trim();
        var email1 = document.getElementsByClassName("cls_em1")[0].value.trim();
        var email2 = document.getElementsByClassName("cls_em2")[0].value.trim();
        var reportsTo = document.getElementById("id_rto").value.trim();
        var assistancenm = document.getElementById("id_anm").value.trim();
        var spousenm = document.getElementById("id_spnm").value.trim();
        var birthdate = document.getElementById("id_bdate").value.trim();
        var anniversarydate = document.getElementById("id_adate").value.trim();
        var designation = document.getElementById("id_de").value.trim();
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
        if (middleName.trim().length != 0) {
            if (middleName.trim().length > 50) {
                fieldSenseTosterError("Middle name can not be more than 50 characters", true);
                return false;
            }
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
            if ((isNaN(phone1))) {
                fieldSenseTosterError("Please enter valid phone number", true);
                return false;
            }
        }
        if (phone2.trim().length != 0) {
            if (phone2.trim().length > 10) {
                fieldSenseTosterError("Phone2 can not be more than 10 digits", true);
                return false;
            }
            if ((isNaN(phone2))) {
                fieldSenseTosterError("Please enter valid phone number", true);
                return false;
            }
        }
        if (phone3.trim().length != 0) {
            if (phone3.trim().length > 10) {
                fieldSenseTosterError("Phone3 can not be more than 10 digits", true);
                return false;
            }
            if ((isNaN(phone2))) {
                fieldSenseTosterError("Please enter valid phone number", true);
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
        // Added by nikhil
        if (email1 != "") {
            var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
            if (!emailPattern.test(email1)) {
                fieldSenseTosterError("Please enter valid email1 .", true);
                return false;
            }
            // Ended by nikhil
            if (email1.trim().length != 0) {
                if (email1.trim().length > 100) {
                    fieldSenseTosterError("Email1 can not be more than 100 characters", true);
                    return false;
                }
            }
        }
        if (email2 != "") {
            var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
            if (!emailPattern.test(email2)) {
                fieldSenseTosterError("Please enter valid email2 .", true);
                return false;
            }
            // Ended by nikhil
            if (email2.trim().length != 0) {
                if (email2.trim().length > 100) {
                    fieldSenseTosterError("Email1 can not be more than 100 characters", true);
                    return false;
                }
            }
            // Ended by nikhil
            if (email2.trim().length != 0) {
                if (email2.trim().length > 100) {
                    fieldSenseTosterError("Email2 can not be more than 100 characters", true);
                    return false;
                }
            }
        }

//        if (email2.trim().length != 0) {
//            if (email2.trim().length > 100) {
//                fieldSenseTosterError("Email2 can not be more than 100 characters", true);

        // Added by nikhil
//        if(email2 !=""){
//            if (!emailPattern.test(email2)) {
//                fieldSenseTosterError("Invalid email address 2.", true);
//>>>>>>> .r7760
//                return false;
//            }
//            // Ended by nikhil
//            if (email2.trim().length != 0) {
//                if (email2.trim().length > 100) {
//                    fieldSenseTosterError("Email2 can not be more than 100 characters", true);
//                    return false;
//                }
//            }
//        }
//         if (!emailPattern.test(email2)) {
//        fieldSenseTosterError("Invalid email address 2.", true);
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
        $('#pleaseWaitDialog').modal('show');
        doubleClickFlag = false;
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
                clevertap.event.push("Edit Contact", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                if (data.errorCode == '0000') {
                    contactgrid(customerId, customerContactId);
                    $('#pleaseWaitDialog').modal('hide');
                    $('.id_editAddcontactTemplate').modal('hide');
                    fieldSenseTosterSuccess(data.errorMessage, true);
                    doubleClickFlag = true;
                } else {
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                    FieldSenseInvalidToken(data.errorMessage);
                    doubleClickFlag = true;
                }
            }
        });
    }
}

function clearId() {
    $('#customertmt').html('');
    // showCustomers();
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
}

function deleteCustomer(customerId, customernm, totalCustomerRecords) {
    //var deletes = confirm("Are you sure you want to delete " + customernm + "");
    //if (deletes == true)
    //{
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
                            if (data.errorCode == '0000') {
                                //added by nikhil
                                clevertap.event.push("Delete Customer", {
//                                    "UserId": loginUserIdcookie,
//                                    "User Name": loginUserNamecookie,
//                                    "AccountId": accountIdcookie,
                                    "Source": "Web",
                                    "Account name": accountNamecookie,
                                    "UserRolecookie": UserRolecookie,
                                });
                                //$('#pleaseWaitDialog').modal('hide');
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                var table = $('#sample_5').DataTable();
                                table.draw(false);
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
    //}
}

function deleteCustomerContact(customerContactId, contactnm, customerId) {
    //var deletes = confirm("Are you sure you want to delete " + contactnm + "");
    // if (deletes == true)
    // {
    bootbox.dialog({
        message: "Are you sure you want to delete " + contactnm + " ?",
        title: "Delete Customer Contact",
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
                            if (data.errorCode == '0000') {
                                clevertap.event.push("Delete Contact", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                                    "Source": "Web",
                                    "Account name": accountNamecookie,
                                    "UserRolecookie": UserRolecookie,
                                });
                                contactgrid(customerId, customerContactId);
                                $('#pleaseWaitDialog').modal('hide');
                                fieldSenseTosterSuccess(data.errorMessage, true);
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
    //}
}

function createContactOnedit(customerId) {
    if (doubleClickFlag) {
        var firstName = document.getElementById("fnm").value.trim();
        var middleName = document.getElementById("mnm").value.trim();
        var lastName = document.getElementById("lnm").value.trim();
        var phone1 = document.getElementById("ph1").value.trim();
        var phone2 = document.getElementById("ph2").value.trim();
        var phone3 = document.getElementById("ph3").value.trim();
        var fax1 = document.getElementById("fx1").value.trim();
        var fax2 = document.getElementById("fx2").value.trim();
        var mobile1 = document.getElementById("mb1").value.trim();
        var mobile2 = document.getElementById("mb2").value.trim();
        var email1 = document.getElementById("em1").value.trim();
        var email2 = document.getElementById("em2").value.trim();
        var reportsTo = document.getElementById("rto").value.trim();
        var assistancenm = document.getElementById("anm").value.trim();
        var spousenm = document.getElementById("spnm").value.trim();
        var birthdate = document.getElementById("bdt").value.trim();
        var anniversarydate = document.getElementById("adt").value.trim();
        var designation = document.getElementById("de").value.trim();
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
        if (middleName.trim().length != 0) {
            if (middleName.trim().length > 50) {
                fieldSenseTosterError("Middle name can not be more than 50 characters", true);
                return false;
            }
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
        if (email2.trim().length != 0) {
            if (email2.trim().length > 100) {
                fieldSenseTosterError("Email2 can not be more than 100 characters", true);
                return false;
            }
        }
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
        $('#pleaseWaitDialog').modal('show');
        doubleClickFlag = false;
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
                if (data.errorCode == '0000') {

                    fieldSenseTosterSuccess(data.errorMessage, true);
                    $('#id_editcontact').html('');
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
                    contactgrid(customerId);
                    doubleClickFlag = true;
                    $('#pleaseWaitDialog').modal('hide');
                } else {
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                    FieldSenseInvalidToken(data.errorMessage);
                    doubleClickFlag = true;
                }
            }
        });
    }
}

function contactgrid(customerId, contactId) {
    $('#id_viewContactTemplate').html('');
    $('#pleaseWaitDialog').modal('show');
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
                    contactShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#responsive2" data-placement="bottom" title="Edit" onclick="editContactTemplate(\'' + customerContactId + '\',\'' + customerId + '\')"><i class="fa fa-edit"></i></button>';
                    contactShowTemplateHtml += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteCustomerContact(\'' + customerContactId + '\',\'' + fullName + '\',\'' + customerId + '\')"><i class="fa fa-trash-o"></i></button>									';
                    contactShowTemplateHtml += '</td>';
                    contactShowTemplateHtml += '</tr>';
                }
                $('#id_viewContactTemplate').append(contactShowTemplateHtml);
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}

function searchCustomerWithSelection() {
    var recentCustomersSub = [];
    var territorycustomersSub = [];
    var industryCustomersSub = [];
    $('#id_searchText').val("");
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
    /*for (var i = 0; i < territoryLength; i++) {
     var territory = document.getElementById("id_Territory_" + i).checked;
     var te = document.getElementById("id_Territory_" + i).value;
     if (territory) {
     index = territorycustomers.indexOf('' + te + '');
     if (index < 0) {
     territorycustomers.push('' + te + '');
     }
     } else {
     index = territorycustomers.indexOf('' + te + '');
     if (index > -1) {
     territorycustomers.splice(index, 1);
     }
     }
     }*/
    for (var j = 0; j < industryLength; j++) {
        var industry = document.getElementById("id_industry_" + j).checked;
        var ind = document.getElementById("id_industry_" + j).value;
        if (industry) {
            index = industryCustomers.indexOf('' + ind + '');
            if (index < 0) {
                industryCustomers.push('' + ind + '');
            }
        } else {
            index = industryCustomers.indexOf('' + ind + '');
            if (index > -1) {
                industryCustomers.splice(index, 1);
            }
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
    for (var i = 0; i < recentCustomers.length; i++) {
        recentCustomersSub.push("recentItems_value=" + recentCustomers[i]);
    }
    for (var i = 0; i < selectedTerritories.length; i++) {
        territorycustomersSub.push("territory_value=" + selectedTerritories[i]);
    }
    for (var i = 0; i < industryCustomers.length; i++) {
        industryCustomersSub.push("industry_value=" + industryCustomers[i]);
    }
    /*var searchObject = {
     "recentItems": recentCustomers,
     "trritory": territorycustomers,
     "industry": industryCustomers
     }
     var jsonData = JSON.stringify(searchObject);*/
    
    var cust_filter_url = fieldSenseURL + "/search/customers/filtering/" + loginUserId;
    if (role == 1 || role == 3 || role == 6 || role == 8) {
     cust_filter_url = fieldSenseURL + "/search/customers/filtering";
    }
    
    var start = 0;
    var length = 20;
    myTable = $("#sample_5").DataTable();
    if (myTable != null)
        myTable.destroy();
    //$('#pleaseWaitDialog').modal('show');
    $('#id_customerdetails').html('');
    var customerShowTemplateHtml = '';
    customerShowTemplateHtml += '<table class="table table-striped table-bordered table-hover" id="sample_5">';
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
    customerShowTemplateHtml += '<tbody></tbody></table>';
    $("#id_customerdetails").html(customerShowTemplateHtml);
    var inboxtable = $('#sample_5').dataTable({
        "bServerSide": true,
        "bDestroy": true,
        "ajax": {
            "url": cust_filter_url,
            "type": "GET",
            headers: {
                "userToken": userToken
            },
            contentType: "application/json; charset=utf-8",
            crossDomain: false,
            /*"data": function ( d ) {
             d.recentItems = recentCustomers;
             d.trritory = territorycustomers;
             d.industry = industryCustomers;
             },*/
            "data": {
                "recentItems": "recentItems_values:" + recentCustomersSub,
                "territory": "territory_values:" + territorycustomersSub,
                "industry": "industry_values:" + industryCustomersSub,
            },
            cache: false,
            dataType: 'json',
            asyn: true,
            error: function (data) {
                if (data.status == 200) {
                    fieldSenseTosterError("Some problem occured.", true);
                    searchCustomerWithSelection();
                } else {
                    $('#pleaseWaitDialog').modal('hide')
                    fieldSenseTosterError(data.responseJSON.errorMessage, true);
                }
                //logout();
            }
        },
        "iDisplayStart": start,
        // set the initial value
        "iDisplayLength": length, // It will be by default page width set by user
        "sPaginationType": "bootstrap_full_number",
        "bProcessing": false,
        "aLengthMenu": [
            [5, 10, 15, 20, 50, 100],
            [5, 10, 15, 20, 50, 100] // change per page values here
        ],
        "oLanguage": {
            // "sProcessing":"Loading",
            "sEmptyTable": "No data available in table",
            "sLengthMenu": "_MENU_ <span id ='id_totalCustRecords'></span>",
            //"sInfo": "Showing _START_ to _END_ of _TOTAL_ Mails",
            "oPaginate": {
                "sPrevious": "Prev",
                "sNext": "Next"
            }
        },
        "aoColumnDefs": [
            {"bSortable": false,
                "aTargets": [1, 5]

            }
        ],
        "aoColumns": [// ahmed : here we populate the data in datatable that is returned using Ajax
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.customerName;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.customerPhone1;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    return full.customerEmail1;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    return full.industry;
                }
            },
            {"mData": null,
                "sClass": "hidden-xs",
                "mRender": function (data, type, full) {
                    return full.territory;
                }
            },
            {"mData": null,
                "sClass": "inbox-small-cells",
                "mRender": function (data, type, full) {
                    var actionButtons = '<button type="button" class="btn btn-default btn-xs tooltips"  data-placement="bottom" title="Edit" onclick="editCustomerDetails(\'' + full.id + '\',\'' + full.customerPrintas + '\');return false;"><i class="fa fa-edit"></i></button>';
                    if (pathName != "customers.html") {
                        actionButtons += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" title="Delete" onclick="deleteCustomer(\'' + full.id + '\',\'' + full.customerName + '\',\'' + full.customersCount + '\')"><i class="fa fa-trash-o"></i></button>';
                    }
                    return actionButtons;
                }
            }
        ],
        "aaSorting": [],
        "bFilter": false,
        "preDrawCallback": function (settings) {
            $('#pleaseWaitDialog').modal('show');
        },
        "fnDrawCallback": function (oSettings) {
            if (oSettings.fnDisplayEnd() > 0) {
                $("#id_totalCustRecords").html("<b>" + (oSettings._iDisplayStart + 1) + "</b> - <b>" + oSettings.fnDisplayEnd() + "</b> of <b>" + oSettings.fnRecordsTotal() + "</b> records");
            }
            inboxtable.$('td').mouseover(function () {
                var sData = $(this).text();
                $(this).attr('title', sData);
            });
            App.initUniform();
            App.fixContentHeight();
            jQuery('.tooltips').tooltip();
            $('#pleaseWaitDialog').modal('hide');
        },
    });

    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown	


}


var activeIndustryData;
var activeTerritoryData;
var parentTerritoryCatagories = [];
var fullTerriCategories = [];

function leftSideMenu() {
    
    var territory_url = fieldSenseURL + "/territoryCategory/active/territory/user/" + loginUserId;
    if (role == 1 || role == 3 || role == 6 || role == 8) {
     territory_url = fieldSenseURL + "/territoryCategory/active/territory";
    }
    
    $.ajax({
        type: "GET",
        //url: fieldSenseURL + "/customer/industry",
        url: fieldSenseURL + "/industryCategory/active",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            var industryData = data.result;
            activeIndustryData = industryData; // to be use in later like on edit customer page
            $.ajax({
                type: "GET",
                //url: fieldSenseURL + "/customer/territory",
                url: territory_url,
                contentType: "application/json; charset=utf-8",
                headers: {
                    "userToken": userToken
                },
                crossDomain: false,
                cache: false,
                dataType: 'json',
                asyn: false,
                success: function (dataTerritory) {
                    var cateArray = [];
                    var territoryData = dataTerritory.result;
                    activeTerritoryData = territoryData; // to be use in later like on edit customer page
                    industryLength = industryData.length;
                    territoryLength = territoryData.length;
                    var leftSideMenuTemplate = '';
                    leftSideMenuTemplate += '<ul class="page-sidebar-menu">';
                    leftSideMenuTemplate += '<li class="start active">';
                    leftSideMenuTemplate += '<span class="title">FILTERS</span>';
                    leftSideMenuTemplate += '</li>';
                    leftSideMenuTemplate += '<li class="opt">';
                    leftSideMenuTemplate += '<span class="title">Recent';
                    leftSideMenuTemplate += '<a href="javascript:;" class="cl cls_clearRecent" style="display:none;" onClick="clearRecentCustomers();">Clear</a>';
                    leftSideMenuTemplate += '</span>';
                    leftSideMenuTemplate += '<div multiple class="form-control multiselect">';
                    leftSideMenuTemplate += '<span class="cls_addRecentCustomerFilter">';
                    leftSideMenuTemplate += '<label><input type="checkbox" class="checkboxes" name="option[]" value="1" id="id_added" onChange="searchCustomerWithSelection();return false;" />Added</label>';
                    leftSideMenuTemplate += '<label><input type="checkbox" class="checkboxes" name="option[]" value="2" id="id_moified" onChange="searchCustomerWithSelection();return false;"/>Modified</label>';
                    leftSideMenuTemplate += '</span>';
                    leftSideMenuTemplate += '</div>';
                    leftSideMenuTemplate += '</li>';
                    leftSideMenuTemplate += '<li class="opt">';
                    leftSideMenuTemplate += '<span class="title">Territory';
                    leftSideMenuTemplate += '<a href="javascript:;" class="cl cls_clearTerritory" style="display:none;" onClick="clearTerritoryCustomers();">Clear</a>';
                    leftSideMenuTemplate += '</span>';
                    leftSideMenuTemplate += '<div multiple class="form-control multiselect" id="territorytree">';
                    //  leftSideMenuTemplate += '<span class="cls_addTerritoryCustomerFilter"><div ></div></span>';

                    leftSideMenuTemplate += '</div>';
                    leftSideMenuTemplate += '</li>';
                    leftSideMenuTemplate += '<li class="opt">';
                    leftSideMenuTemplate += '<span class="title">Industry';
                    leftSideMenuTemplate += '<a href="javascript:;" class="cl cls_clearIndustry" style="display:none;" onClick="clearIndustryCustomers();">Clear</a>';
                    leftSideMenuTemplate += '</span>';
                    leftSideMenuTemplate += '<div multiple class="form-control multiselect">';
                    leftSideMenuTemplate += '<span class="cls_addIndustryCustomerFilter">';
                    for (var j = 0; j < industryData.length; j++) {
                        leftSideMenuTemplate += '<label><input type="checkbox" class="checkboxes" name="option[]" value="' + industryData[j].categoryName + '" id="id_industry_' + j + '" onChange="searchCustomerWithSelection();return false;"/>' + industryData[j].categoryName + '</label>';
                    }
                    leftSideMenuTemplate += '</span>';
                    leftSideMenuTemplate += '</div>';
                    leftSideMenuTemplate += '</li>';
                    leftSideMenuTemplate += '<a href="#" class="btn btn-warning" onclick="showCustomers()">Clear Filters</a>';
                    leftSideMenuTemplate += '<li class="last">';
                    leftSideMenuTemplate += '</li>';
                    leftSideMenuTemplate += '</ul>';
                    $('#id_leftMenu').html(leftSideMenuTemplate);
                    for (var i = 0; i < territoryData.length; i++) {
                        var id = territoryData[i].id;
                        var parentId = territoryData[i].parentCategory;
                        var categoryName = territoryData[i].categoryName;
                        var hasChild = territoryData[i].hasChild;
                        var catCsv = territoryData[i].categoryPositionCsv;
                        if (territoryData[i].parentCategory == 0) {
                            if (territoryData[i].hasChild == 0) {
                                parentTerritoryCatagories.push({"id": id, "categoryName": categoryName, "categoryPositionCsv": catCsv});
                            } else {
                                parentTerritoryCatagories.push({"id": id, "categoryName": categoryName, "categoryPositionCsv": catCsv, "list": [{"id": id, "categoryName": "unknown"}]});
                            }
                        } else {
                            fullTerriCategories.push({"parentCat": parentId, "id": id, "categoryName": categoryName, "hasChild": hasChild, "categoryPositionCsv": catCsv});
                        }

                        cateArray.push({"id": id, "parentid": parentId, "name": "<input type=\"checkbox\"  name=\"option[]\" value=\"" + categoryName + "\" id=\"id_Territory_" + id + "\" onchange=\"EnableCheckboxes('" + id + "','" + categoryName + "','" + hasChild + "','" + catCsv + "');searchCustomerWithSelection();return false;\">" + categoryName});
                        // leftSideMenuTemplate += '<label><input type="checkbox" class="checkboxes" name="option[]" value="' + territoryData[i].categoryName + '" id="id_Territory_' + i + '" onChange="searchCustomerWithSelection();return false;"/>' + territoryData[i].categoryName + '</label>';
                    }
                    if (territoryData.length != 0) {
                        var source =
                                {
                                    dataType: "json",
                                    dataFields: [
                                        {name: "name", type: "string"},
                                        {name: "id", type: "number"},
                                        {name: "parentid", type: "number"},
                                    ],
                                    hierarchy:
                                            {
                                                keyDataField: {name: 'id'},
                                                parentDataField: {name: 'parentid'}
                                            },
                                    id: 'id',
                                    localData: cateArray
                                };
                        var dataAdapter = new $.jqx.dataAdapter(source);
                        // create Tree Grid
                        $("#territorytree").jqxTreeGrid(
                                {
                                    width: 180,
                                    source: dataAdapter,
                                    sortable: true,
                                    //pageable: true,
                                    pagerMode: 'advanced',
                                    ready: function () {
                                        $("#territorytree").jqxTreeGrid('expandRow', '2');
                                        $("#territorytree").removeClass("jqx-grid ");
                                        $("#contentterritorytree > .jqx-grid-header").hide();
                                        $("#tableterritorytree > tbody > tr > td").each(function () {
                                            $(this).removeClass("jqx-grid-cell");
                                        });
                                        App.initUniform();
                                        App.fixContentHeight();
                                        App.callHandleScrollers();
                                        jQuery('.tooltips').tooltip();
                                    },
                                    columns: [
                                        {text: ' ', dataField: "name"},
                                    ]
                                });
                        $('#territorytree').on('rowCollapse', function (event) {
                            $("#territorytree").removeClass("jqx-grid ");
                            $("#contentterritorytree > .jqx-grid-header").hide();
                            $("#tableterritorytree > tbody > tr > td").each(function () {
                                $(this).removeClass("jqx-grid-cell");
                            });
                            App.initUniform();
                            App.fixContentHeight();
                            App.callHandleScrollers();
                            jQuery('.tooltips').tooltip();
                            for (var i = 0; i < territorycustomers.length; i++) {
                                if (territorycustomers[i].checked == true) {
                                    $("#id_Territory_" + territorycustomers[i].catid).parent().attr("class", "checked");
                                    $("#id_Territory_" + territorycustomers[i].catid).attr('checked', true);
                                }
                            }

                        });
                        $('#territorytree').on('rowExpand', function (event) {
                            $("#territorytree").removeClass("jqx-grid ");
                            $("#contentterritorytree > .jqx-grid-header").hide();
                            $("#tableterritorytree > tbody > tr > td").each(function () {
                                $(this).removeClass("jqx-grid-cell");
                            });
                            App.initUniform();
                            App.fixContentHeight();
                            App.callHandleScrollers();
                            jQuery('.tooltips').tooltip();
                            for (var i = 0; i < territorycustomers.length; i++) {
                                if (territorycustomers[i].checked == true) {
                                    $("#id_Territory_" + territorycustomers[i].catid).parent().attr("class", "checked");
                                    $("#id_Territory_" + territorycustomers[i].catid).attr('checked', true);
                                }
                            }
                        });
                        $("#territorytree").removeClass("jqx-grid ");
                        $("#contentterritorytree > .jqx-grid-header").hide();
                        $("#tableterritorytree > tbody > tr > td").each(function () {
                            $(this).removeClass("jqx-grid-cell");
                        });
                    }
                    App.initUniform();
                    App.fixContentHeight();
                    App.callHandleScrollers();
                    jQuery('.tooltips').tooltip();
                }
            });
        }
    });
}

var selectedTerritories = new Array();
function EnableCheckboxes(id, catName, hasChild, catCsv) {
    var id = parseInt(id);
    if ($("#id_Territory_" + id).is(":checked")) {
        ///index = a.findIndex(x => x.prop2=="yutu")
        var index = $.map(territorycustomers, function (obj, index) {
            if (obj.catid == id) {
                return index;
            }
        });
        if (index.length == 0) {
            territorycustomers.push({"catid": id, "checked": true, "categoryName": catName});
            selectedTerritories.push(catName);
        }
        if (hasChild == "1") {
            var childCat = [];
            for (var i = 0; i < fullTerriCategories.length; i++) {
                var parentCsvindex = fullTerriCategories[i].categoryPositionCsv.lastIndexOf("," + catCsv);
                if (parentCsvindex != -1) {
                    var parentPos = fullTerriCategories[i].categoryPositionCsv.substring(parentCsvindex + 1);
                    if (catCsv == parentPos) {
                        childCat.push({"id": fullTerriCategories[i].id, "checked": true, "categoryName": fullTerriCategories[i].categoryName});
                    }
                }
            }
            for (var i = 0; i < childCat.length; i++) {
                var index = $.map(territorycustomers, function (obj, index) {
                    if (obj.catid == childCat[i].id) {
                        return index;
                    }
                });
                if (index.length == 0) {
                    territorycustomers.push({"catid": childCat[i].id, "checked": true, "categoryName": childCat[i].categoryName});
                    selectedTerritories.push(childCat[i].categoryName);
                    $("#id_Territory_" + childCat[i].id).parent().attr("class", "checked");
                    $("#id_Territory_" + childCat[i].id).attr('checked', true);
                }
            }
        }
    } else {
        var index = $.map(territorycustomers, function (obj, index) {
            if (obj.catid == id) {
                return index;
            }
        });
        if (index.length != 0) {
            territorycustomers.splice(index[0], 1);
            selectedTerritories.splice(index[0], 1);
        }
        if (hasChild == "1") {
            var childCat = [];
            for (var i = 0; i < fullTerriCategories.length; i++) {
                var parentCsvindex = fullTerriCategories[i].categoryPositionCsv.lastIndexOf("," + catCsv);
                if (parentCsvindex != -1) {
                    var parentPos = fullTerriCategories[i].categoryPositionCsv.substring(parentCsvindex + 1);
                    if (catCsv == parentPos) {
                        childCat.push({"id": fullTerriCategories[i].id, "checked": true, "categoryName": fullTerriCategories[i].categoryName});
                    }
                }
            }
            for (var i = 0; i < childCat.length; i++) {
                var index = $.map(territorycustomers, function (obj, index) {
                    if (obj.catid == childCat[i].id) {
                        return index;
                    }
                });
                if (index.length != 0) {
                    territorycustomers.splice(index[0], 1);
                    selectedTerritories.splice(index[0], 1);
                    $("#id_Territory_" + childCat[i].id).parent().removeClass("checked");
                    $("#id_Territory_" + childCat[i].id).attr('checked', false);
                }
            }
        }
    }
}


function clearRecentCustomers() {
    recentCustomers = new Array();
    $('.cls_clearRecent').hide();
    $("#id_added").attr('checked', false);
    $("#id_added").parent().removeClass('checked');
    $("#id_moified").attr('checked', false);
    $("#id_moified").parent().removeClass('checked');
    $('#id_searchText').val("");
    searchCustomerWithSelection();
}

function clearIndustryCustomers() {
    industryCustomers = new Array();
    $('[id^=id_industry_]').each(function () {
        $(this).attr('checked', false);
        $(this).parent().removeClass('checked');
    });
    $('#id_searchText').val("");
    searchCustomerWithSelection();

}

function clearTerritoryCustomers() {
    territorycustomers = new Array();
    selectedTerritories = new Array();
    $('.cls_clearTerritory').hide();
    $('[id^=id_Territory_]').each(function () {
        $(this).attr('checked', false);
        $(this).parent().removeClass('checked');
    });
    $('#id_searchText').val("");
    searchCustomerWithSelection();

}

function dataTableForCustomerDetails() {
    var oTable = $('#sample_5').dataTable({
        "aoColumnDefs": [
            {
                "bSortable": false,
                "aTargets": [5]
            }
        ],
        "aaSorting": [[2, 'asc']],
        "aLengthMenu": [
            [-1, 5, 10, 15, 20],
            ["All", 5, 10, 15, 20] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 10,
        "bFilter": false
    });
    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

    $('#sample_5_column_toggler input[type="checkbox"]').change(function () {
        /* Get the DataTables object again - this is not a recreation, just a get of the object */
        var iCol = parseInt($(this).attr("data-column"));
        var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
        oTable.fnSetColumnVis(iCol, (bVis ? false : true));
    });
}
function dataTableForCustomerDetailsLogin() {
    var oTable = $('#sample_5').dataTable({
        "aoColumnDefs": [
            {
                "bSortable": false,
                "aTargets": [5]
            }
        ],
        "aaSorting": [[0, 'asc']],
        "aLengthMenu": [
            [-1, 5, 10, 15, 20],
            ["All", 5, 10, 15, 20] // change per page values here
        ],
        // set the initial value
        "iDisplayLength": 10,
        "bFilter": false,
        "bPaginate": false
    });
    jQuery('#sample_5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
    jQuery('#sample_5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
    jQuery('#sample_5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

    $('#sample_5_column_toggler input[type="checkbox"]').change(function () {
        /* Get the DataTables object again - this is not a recreation, just a get of the object */
        var iCol = parseInt($(this).attr("data-column"));
        var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
        oTable.fnSetColumnVis(iCol, (bVis ? false : true));
    });
}

function editContactTemplate(customerContactId, customerId) {
    $('#pleaseWaitDialog').modal('show');
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
                var editContactTemplate = '';
                editContactTemplate += '<div class="modal-dialog modal-wide edt_contact">';
                editContactTemplate += '<div class="modal-content">';
                editContactTemplate += '<div class="modal-header">';
                editContactTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                editContactTemplate += '<h4 class="modal-title">Edit Contact</h4>';
                editContactTemplate += '</div>';
                editContactTemplate += '<form class="form-horizontal" role="form">';
                editContactTemplate += '<div class="modal-body" id="act">';
                editContactTemplate += '<div class="scroller" style="max-height:700px" data-always-visible="1" data-rail-visible1="1">';
                editContactTemplate += '<div class="form-body">';
                editContactTemplate += '<div class="row">';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">First Name</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="fname" class="form-control" placeholder="' + firstName + '" tabindex="1" value="' + firstName + '" id="id_fnm">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Mobile1</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Mobile1" class="form-control cls_mb1" placeholder="Enter Mobile1" id="mask_mobile" tabindex="9" value="' + mobile1 + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="row">';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Middle Name</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="mname" class="form-control" placeholder="Enter Middle Name" tabindex="2" id="id_mnm" value="' + middleName + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Mobile2</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Mobile2" class="form-control cls_mb2" placeholder="Enter Mobile2" id="mask_mobile1" tabindex="10" value="' + mobile2 + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="row">';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Last Name</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="lname" class="form-control" placeholder="Enter Last Name" tabindex="3" id="id_lnm" value="' + lastName + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Phone1</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Phone2" class="form-control cls_ph1" placeholder="Enter Phone1" id="mask_phone2" tabindex="11" value="' + phone1 + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="row">';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Designation</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Designation" class="form-control" placeholder="Enter Designation" tabindex="4" id="id_de" value="' + designation + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Phone2</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Phone3" class="form-control cls_ph2" placeholder="Enter Phone2" id="mask_phone3" tabindex="12" value="' + phone1 + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="row">';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Reports To</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Reports To" class="form-control" placeholder="Enter Reports To" tabindex="5" id="id_rto" value="' + reportsTo + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Phone3</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Phone3" class="form-control cls_ph3" placeholder="Enter Phone3" id="mask_phone3" tabindex="12" value="' + phone3 + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="row">';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Fax1</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Fax" class="form-control cls_fx1" placeholder="Enter Fax" id="mask_fax" tabindex="13" value="' + fax1 + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Fax2</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Fax" class="form-control cls_fx2" placeholder="Enter Fax" id="mask_fax" tabindex="13" value="' + fax2 + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="row">';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Assistant Name</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Assistant Name" class="form-control" placeholder="Enter Assistant Name" tabindex="6" id="id_anm" value="' + assistancenm + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Email1</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Email1" class="form-control cls_em1" placeholder="Enter Email1" id="inputEmail1" tabindex="14" value="' + email1 + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="row">';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Date of Birth</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input class="form-control date-picker" data-date-format="dd-mm-yyyy" size="16" type="text" placeholder="Select date" tabindex="7" id="id_bdate" value="' + bdate + '"/>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Email2</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Email2" class="form-control cls_em2" placeholder="Enter Email2" id="inputEmail2" tabindex="15" value="' + email2 + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="row">';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Spouse Name</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input type="Spouse Name" class="form-control" placeholder="Enter Spouse Name" tabindex="8" id="id_spnm" value="' + spousenm + '">';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="col-md-6 form-group">';
                editContactTemplate += '<label class="col-md-5 control-label">Anniversary Date</label>';
                editContactTemplate += '<div class="col-md-7">';
                editContactTemplate += '<input class="form-control date-picker" data-date-format="dd-mm-yyyy" size="16" type="text" placeholder="Select date" tabindex="16" id="id_adate" value="' + adate + '"/>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                editContactTemplate += '<div class="modal-footer">';
                editContactTemplate += '<button type="button" class="btn btn-info" tabindex="17" onClick="editContact(\'' + customerContactId + '\',\'' + customerId + '\');return false;">Save</button>';
                editContactTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="18">Cancel</button>';
                editContactTemplate += '</div>';
                editContactTemplate += '</form>';
                editContactTemplate += '</div>';
                editContactTemplate += '</div>';
                $('.id_editAddcontactTemplate').html(editContactTemplate);
                $('#pleaseWaitDialog').modal('hide');
                App.init();
                $('.date-picker').datepicker({
                    rtl: App.isRTL(),
                    autoclose: true
                });
            }
        }
    });
}

function createNotes(customerId) {
    var description = document.getElementById("id_notes").value.trim();
    if (description.trim().length == 0) {
        fieldSenseTosterError("Notes cannot be empty", true);
        return false;
    }
    description = description.trim();
    var notesObject = {
        "userId": {
            "id": loginUserId
        },
        "customerId": customerId,
        "description": description
    }
    var jsonData = JSON.stringify(notesObject);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/notes",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var notesData = data.result;
            if (data.errorCode == '0000') {
                clevertap.event.push(" Add Note", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });

                $('#id_notes').val('');
                $(".cls_notes").modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                var note = notesData.description;
                var id = notesData.id;
                var firstNm = notesData.userId.firstName;
                var lastNm = notesData.userId.lastName;
                var createdOn = notesData.createdOn;
                createdOn = fieldSenseDateTimeForNotes(createdOn);
                var viewNotesTemplate = '';
                var fullNm = firstNm + ' ' + lastNm;
                var id_comments = 'comments' + id;
                viewNotesTemplate += '<div class="form-group comm">';
                viewNotesTemplate += '<div class="col-md-12">';
                viewNotesTemplate += '<label id="' + id_comments + '">';
                viewNotesTemplate += note;
                viewNotesTemplate += '</label></div>';
                viewNotesTemplate += '<label class="col-md-12">';
                viewNotesTemplate += '<span class="time-note">Added by ' + fullNm + ' on ' + createdOn + '</span>';
                viewNotesTemplate += '<button type="button" class="btn btn-xs btn-default" data-toggle="modal" href="#responsive5" onclick="editNotesTemplate(\'' + customerId + '\',\'' + id + '\')"><i class="fa fa-edit"></i></button></label>';
                viewNotesTemplate += '</div>';
                $('.cls_viewNotesOneCustomer').prepend(viewNotesTemplate);
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}

function addActivityInCustomerTemplate(customerId, customerName) {
    $('#pleaseWaitDialog').modal('show');
    if (role == 5) {
        $.ajax({
            type: "GET",
            url: fieldSenseURL + "/activityPurpose",
            contentType: "application/json; charset=utf-8",
            headers: {
                "userToken": userToken
            },
            crossDomain: false,
            cache: false,
            dataType: 'json',
            asyn: true,
            success: function (data) {
                if (data.errorCode == '0000') {
                    var purposeData = data.result;
                    $.ajax({
                        type: "GET",
                        url: fieldSenseURL + "/team/TL",
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        asyn: true,
                        success: function (datateamLead) {
                            if (datateamLead.errorCode == '0000') {
                                var datateamLead = datateamLead.result;
                                $.ajax({
                                    type: "GET",
                                    url: fieldSenseURL + "/team/userPositions/" + loginUserId,
                                    contentType: "application/json; charset=utf-8",
                                    headers: {
                                        "userToken": userToken
                                    },
                                    crossDomain: false,
                                    cache: false,
                                    dataType: 'json',
                                    asyn: true,
                                    success: function (datateamMember) {
                                        if (datateamMember.errorCode == '0000') {
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
                                                success: function (dataCustomerContact) {
                                                    if (dataCustomerContact.errorCode == '0000') {
                                                        var teamMemberData = datateamMember.result;
                                                        var activityTemplate = '';
                                                        activityTemplate += '<div class="modal-dialog modal-wide">';
                                                        activityTemplate += '<div class="modal-content">';
                                                        activityTemplate += '<div class="modal-header">';
                                                        activityTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                                                        activityTemplate += '<h4 class="modal-title">Add Visit</h4>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<form class="form-horizontal form-row-seperated">';
                                                        activityTemplate += '<div class="modal-body">';
                                                        activityTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" id="id_createactivity">';
                                                        activityTemplate += '<div class="form-body">';
                                                        activityTemplate += '<div class="row">';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Customer Name<span style="color:red"> *</span></label>';
                                                        activityTemplate += '<div class="col-md-7">';
                                                        activityTemplate += '<input type="hidden" id="id_hidden" value="' + customerId + '"/>';
                                                        activityTemplate += '<input type="Cname" name="currency" class="form-control" id="autocompleteCnm" placeholder="Enter text" value="' + customerName + '" disabled>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Contact<span style="color:red"> *</span></label>';
                                                        activityTemplate += '<div class="col-md-7">';
                                                        activityTemplate += '<select class="form-control" id="id_contact">';
                                                        var dataCustomerContact = dataCustomerContact.result;
                                                        activityTemplate += '<option>None</option>';
                                                        for (var i = 0; i < dataCustomerContact.length; i++) {
                                                            var contId = dataCustomerContact[i].id;
                                                            var firstName = dataCustomerContact[i].firstName;
                                                            var middleName = dataCustomerContact[i].middleName;
                                                            var lastName = dataCustomerContact[i].lastName;
                                                            var fullName = firstName + ' ' + middleName + ' ' + lastName;
                                                            activityTemplate += '<option value="' + contId + '">' + fullName + '</option>';
                                                        }
                                                        activityTemplate += '</select>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="row">';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Title<span style="color:red"> *</span></label>';
                                                        activityTemplate += '<div class="col-md-7">';
                                                        activityTemplate += '<input type="activity" class="form-control" placeholder="Enter text" id="id_activity">';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Date</label>';
                                                        activityTemplate += '<div class="col-md-7">';
                                                        var today = new Date();
                                                        var dd = today.getDate();
                                                        var mm = today.getMonth() + 1; //January is 0!

                                                        var yyyy = today.getFullYear();
                                                        if (dd < 10) {
                                                            dd = '0' + dd
                                                        }
                                                        if (mm < 10) {
                                                            mm = '0' + mm
                                                        }
                                                        var currentHr = today.getHours();
                                                        var currentMin = today.getMinutes();
                                                        var today = dd + '-' + mm + '-' + yyyy;
                                                        activityTemplate += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
                                                        activityTemplate += '<input type="text" class="form-control" id="id_date" readonly value="' + today + '">';
                                                        activityTemplate += '<span class="input-group-btn">';
                                                        activityTemplate += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                                                        activityTemplate += '</span>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="row">';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Start Time</label>';
                                                        activityTemplate += '<div class="col-md-3">';
                                                        activityTemplate += '<select id="activity_hr" class="form-control input-xssmall">';
                                                        activityTemplate += '<option value="HH">HH</option>';
                                                        for (var j = 0; j < 24; j++) {
                                                            if (j < 10)
                                                            {
                                                                if (currentHr == j) {
                                                                    activityTemplate += '<option value="' + j + '" selected>0' + j + '</option>';
                                                                }
                                                                else {
                                                                    activityTemplate += '<option value="' + j + '">0' + j + '</option>';
                                                                }
                                                            } else {
                                                                if (currentHr == j) {
                                                                    activityTemplate += '<option value="' + j + '" selected>' + j + '</option>';
                                                                }
                                                                else {
                                                                    activityTemplate += '<option value="' + j + '">' + j + '</option>';
                                                                }
                                                            }
                                                        }
                                                        activityTemplate += '</select></div>';
                                                        activityTemplate += '<div class="col-md-3">';
                                                        activityTemplate += '<select id="activity_min" class="form-control input-xssmall">';
                                                        activityTemplate += '<option value="MM">MM</option>';
                                                        for (var k = 0; k < 60; k = k + 1) {
                                                            if (k < 10)
                                                            {
                                                                if (currentMin == k) {
                                                                    activityTemplate += '<option value="' + k + '" selected>0' + k + '</option>';
                                                                } else {
                                                                    activityTemplate += '<option value="' + k + '">0' + k + '</option>';
                                                                }
                                                            }
                                                            else {
                                                                if (currentMin == k) {
                                                                    activityTemplate += '<option value="' + k + '"selected>' + k + '</option>';
                                                                } else {
                                                                    activityTemplate += '<option value="' + k + '">' + k + '</option>';
                                                                }
                                                            }
                                                        }
                                                        activityTemplate += '</select>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">End Time</label>';
                                                        activityTemplate += '<div class="col-md-3">';
                                                        activityTemplate += '<select id="activity_endHr" class="form-control input-xssmall">';
                                                        activityTemplate += '<option value="HH">HH</option>';
                                                        for (var l = 0; l < 24; l++) {
                                                            if (l < 10)
                                                            {
                                                                if (currentHr + 1 == l) {
                                                                    activityTemplate += '<option value="' + l + '" selected>0' + l + '</option>';
                                                                } else {
                                                                    activityTemplate += '<option value="' + l + '">0' + l + '</option>';
                                                                }
                                                            } else {
                                                                if (currentHr + 1 == l) {
                                                                    activityTemplate += '<option value="' + l + '" selected>' + l + '</option>';
                                                                } else {
                                                                    activityTemplate += '<option value="' + l + '">' + l + '</option>';
                                                                }
                                                            }
                                                        }
                                                        activityTemplate += '</select></div>';
                                                        activityTemplate += '<div class="col-md-3">';
                                                        activityTemplate += '<select id="activity_endMin" class="form-control input-xssmall">';
                                                        activityTemplate += '<option value="MM">MM</option>';
                                                        for (var m = 0; m < 60; m = m + 1) {
                                                            if (m < 10)
                                                            {
                                                                if (currentMin == m) {
                                                                    activityTemplate += '<option value="' + m + '" selected>0' + m + '</option>';
                                                                } else {
                                                                    activityTemplate += '<option value="' + m + '">0' + m + '</option>';
                                                                }
                                                            } else {
                                                                if (currentMin == m) {
                                                                    activityTemplate += '<option value="' + m + '" selected>' + m + '</option>';
                                                                } else {
                                                                    activityTemplate += '<option value="' + m + '">' + m + '</option>';
                                                                }
                                                            }
                                                        }
                                                        activityTemplate += '</select>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="row">';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Purpose</label>';
                                                        activityTemplate += '<div class="col-md-7">';
                                                        activityTemplate += '<select class="form-control" id="id_purpose">';
                                                        for (var i = 0; i < purposeData.length; i++) {
                                                            var id = purposeData[i].id;
                                                            var purpose = purposeData[i].purpose;
                                                            activityTemplate += '<option value="' + id + '">' + purpose + '</option>';
                                                        }
                                                        activityTemplate += '</select>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="row">';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Owner</label>';
                                                        activityTemplate += '<div class="col-md-7">';
                                                        activityTemplate += '<select class="form-control" id="id_owner" disabled>';
                                                        activityTemplate += '<option value="' + loginUserId + '" selected>' + loginUserName + '</option>';
                                                        activityTemplate += '</select>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Assigned To</label>';
                                                        activityTemplate += '<div class="col-md-7">';
                                                        activityTemplate += '<select class="form-control" id="id_assigned">';
//                                                        activityTemplate += '<select class="form-control" id="id_assigned" multiple>'; // multiple selection added by jyoti 13-july-2017
                                                        activityTemplate += '<option>None</option>';
                                                        for (var i = 0; i < teamMemberData.length; i++) {
                                                            var userId = teamMemberData[i].user.id;
                                                            if (loginUserId != userId) {
                                                                var firstName = teamMemberData[i].user.firstName;
                                                                var lastName = teamMemberData[i].user.lastName;
                                                                var fullName = firstName + ' ' + lastName;
                                                                activityTemplate += '<option value="' + userId + '">' + fullName + '</option>';
                                                            }
                                                        }
                                                        activityTemplate += '</select>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="row">';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Status</label>';
                                                        activityTemplate += '<div class="col-md-7">';
                                                        activityTemplate += '<select class="form-control" id="id_status" onchange="outcomeForAddActivity()">';
                                                        activityTemplate += '<option>Select</option>';
                                                        activityTemplate += '<option value="2">Completed</option>';
                                                        activityTemplate += '<option value="0" selected>Pending</option>';
                                                        activityTemplate += '<option value="1">In-Progress</option>';
                                                        activityTemplate += '<option value="3">Cancelled</option>';
                                                        activityTemplate += '</select>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="col-md-6 form-group">';
                                                        activityTemplate += '<label class="col-md-5 control-label">Outcome</label>';
                                                        activityTemplate += '<div class="col-md-7">';
                                                        activityTemplate += '<select class="form-control" id="id_outcome">';
                                                        activityTemplate += '<option value="0" disabled>None</option>';
                                                        activityTemplate += '</select>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="row">';
                                                        //                                                        activityTemplate += '<div class="form-group" id="dscp">';
                                                        activityTemplate += '<div class="col-md-12 form-group" id="dscp">';
                                                        activityTemplate += '<label class="col-md-3 control-label">Description</label>';
                                                        activityTemplate += '<div class="col-md-9">';
                                                        activityTemplate += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_description"></textarea>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<span id="id_outcomedescriptiontextbox" style="display:none;">';
                                                        activityTemplate += '<div class="row">';
                                                        activityTemplate += '<div class="col-md-12 form-group" id="dscp">';
                                                        activityTemplate += '<label class="col-md-3 control-label">Outcome Description</label>';
                                                        activityTemplate += '<div class="col-md-9">';
                                                        activityTemplate += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_outcomedescription"></textarea>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</span>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '<div class="modal-footer">';
                                                        activityTemplate += '<button type="submit" class="btn btn-info" onclick="createActivityInCustomer();return false;">Save</button>';
                                                        activityTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</form>';
                                                        activityTemplate += '</div>';
                                                        activityTemplate += '</div>';
                                                        $('.cls_addActivityInCustomer').html(activityTemplate);
                                                        $('#pleaseWaitDialog').modal('hide');
                                                        $('#id_createactivity').slimScroll({
                                                            size: '7px',
                                                            color: '#bbbbbb',
                                                            opacity: .9,
                                                            position: false ? 'left' : 'right',
                                                            height: 420,
                                                            allowPageScroll: false,
                                                            disableFadeOut: false
                                                        });
                                                        //                                                        App.init();
                                                        $('.date-picker').datepicker({
                                                            rtl: App.isRTL(),
                                                            autoclose: true
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    } else if (role == 1 || role == 3 || role == 6 || role == 8) {
        $.ajax({
            type: "GET",
            url: fieldSenseURL + "/activityPurpose",
            contentType: "application/json; charset=utf-8",
            headers: {
                "userToken": userToken
            },
            crossDomain: false,
            cache: false,
            dataType: 'json',
            asyn: true,
            success: function (data) {
                if (data.errorCode == '0000') {
                    var purposeData = data.result;
                    $.ajax({
                        type: "GET",
                        url: fieldSenseURL + "/team/TL",
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        asyn: true,
                        success: function (datateamLead) {
                            if (datateamLead.errorCode == '0000') {
                                var datateamLead = datateamLead.result;

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
                                    success: function (dataCustomerContact) {
                                        if (dataCustomerContact.errorCode == '0000') {
                                            var activityTemplate = '';
                                            activityTemplate += '<div class="modal-dialog modal-wide">';
                                            activityTemplate += '<div class="modal-content">';
                                            activityTemplate += '<div class="modal-header">';
                                            activityTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                                            activityTemplate += '<h4 class="modal-title">Add Visit</h4>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<form class="form-horizontal form-row-seperated">';
                                            activityTemplate += '<div class="modal-body">';
                                            activityTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" id="id_createactivity">';
                                            activityTemplate += '<div class="form-body">';
                                            activityTemplate += '<div class="row">';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Customer Name<span style="color:red"> *</span></label>';
                                            activityTemplate += '<div class="col-md-7">';
                                            activityTemplate += '<input type="hidden" id="id_hidden" value="' + customerId + '"/>';
                                            activityTemplate += '<input type="Cname" name="currency" class="form-control" id="autocompleteCnm" placeholder="Enter text" value="' + customerName + '" disabled>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Contact<span style="color:red"> *</span></label>';
                                            activityTemplate += '<div class="col-md-7">';
                                            activityTemplate += '<select class="form-control" id="id_contact">';
                                            var dataCustomerContact = dataCustomerContact.result;
                                            activityTemplate += '<option>None</option>';
                                            for (var i = 0; i < dataCustomerContact.length; i++) {
                                                var contId = dataCustomerContact[i].id;
                                                var firstName = dataCustomerContact[i].firstName;
                                                var middleName = dataCustomerContact[i].middleName;
                                                var lastName = dataCustomerContact[i].lastName;
                                                var fullName = firstName + ' ' + middleName + ' ' + lastName;
                                                activityTemplate += '<option value="' + contId + '">' + fullName + '</option>';
                                            }
                                            activityTemplate += '</select>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="row">';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Title</label>';
                                            activityTemplate += '<div class="col-md-7">';
                                            activityTemplate += '<input type="activity" class="form-control" placeholder="Enter text" id="id_activity">';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Date<span style="color:red"> *</span></label>';
                                            activityTemplate += '<div class="col-md-7">';
                                            var today = new Date();
                                            var dd = today.getDate();
                                            var mm = today.getMonth() + 1; //January is 0!

                                            var yyyy = today.getFullYear();
                                            if (dd < 10) {
                                                dd = '0' + dd
                                            }
                                            if (mm < 10) {
                                                mm = '0' + mm
                                            }
                                            var currentHr = today.getHours();
                                            var currentMin = today.getMinutes();
                                            var today = dd + '-' + mm + '-' + yyyy;
                                            activityTemplate += '<div class="input-group date date-picker" data-date-format="dd-mm-yyyy" data-date-start-date="-1m">';
                                            activityTemplate += '<input type="text" class="form-control" id="id_date" readonly value="' + today + '">';
                                            activityTemplate += '<span class="input-group-btn">';
                                            activityTemplate += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
                                            activityTemplate += '</span>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="row">';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Start Time<span style="color:red"> *</span></label>';
                                            activityTemplate += '<div class="col-md-3">';
                                            activityTemplate += '<select id="activity_hr" class="form-control input-xssmall">';
                                            activityTemplate += '<option value="HH">HH</option>';
                                            for (var j = 0; j < 24; j++) {
                                                if (j < 10)
                                                {
                                                    if (currentHr == j) {
                                                        activityTemplate += '<option value="' + j + '" selected>0' + j + '</option>';
                                                    }
                                                    else {
                                                        activityTemplate += '<option value="' + j + '">0' + j + '</option>';
                                                    }
                                                } else {
                                                    if (currentHr == j) {
                                                        activityTemplate += '<option value="' + j + '" selected>' + j + '</option>';
                                                    }
                                                    else {
                                                        activityTemplate += '<option value="' + j + '">' + j + '</option>';
                                                    }
                                                }
                                            }
                                            activityTemplate += '</select></div>';
                                            activityTemplate += '<div class="col-md-3">';
                                            activityTemplate += '<select id="activity_min" class="form-control input-xssmall">';
                                            activityTemplate += '<option value="MM">MM</option>';
                                            for (var k = 0; k < 60; k = k + 1) {
                                                if (k < 10)
                                                {
                                                    if (currentMin == k) {
                                                        activityTemplate += '<option value="' + k + '" selected>0' + k + '</option>';
                                                    } else {
                                                        activityTemplate += '<option value="' + k + '">0' + k + '</option>';
                                                    }
                                                }
                                                else {
                                                    if (currentMin == k) {
                                                        activityTemplate += '<option value="' + k + '"selected>' + k + '</option>';
                                                    } else {
                                                        activityTemplate += '<option value="' + k + '">' + k + '</option>';
                                                    }
                                                }
                                            }
                                            activityTemplate += '</select>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">End Time<span style="color:red"> *</span></label>';
                                            activityTemplate += '<div class="col-md-3">';
                                            activityTemplate += '<select id="activity_endHr" class="form-control input-xssmall">';
                                            activityTemplate += '<option value="HH">HH</option>';
                                            for (var l = 0; l < 24; l++) {
                                                if (l < 10)
                                                {
                                                    if (currentHr + 1 == l) {
                                                        activityTemplate += '<option value="' + l + '" selected>0' + l + '</option>';
                                                    } else {
                                                        activityTemplate += '<option value="' + l + '">0' + l + '</option>';
                                                    }
                                                } else {
                                                    if (currentHr + 1 == l) {
                                                        activityTemplate += '<option value="' + l + '" selected>' + l + '</option>';
                                                    } else {
                                                        activityTemplate += '<option value="' + l + '">' + l + '</option>';
                                                    }
                                                }
                                            }
                                            activityTemplate += '</select></div>';
                                            activityTemplate += '<div class="col-md-3">';
                                            activityTemplate += '<select id="activity_endMin" class="form-control input-xssmall">';
                                            activityTemplate += '<option value="MM">MM</option>';
                                            for (var m = 0; m < 60; m = m + 1) {
                                                if (m < 10)
                                                {
                                                    if (currentMin == m) {
                                                        activityTemplate += '<option value="' + m + '" selected>0' + m + '</option>';
                                                    } else {
                                                        activityTemplate += '<option value="' + m + '">0' + m + '</option>';
                                                    }
                                                } else {
                                                    if (currentMin == m) {
                                                        activityTemplate += '<option value="' + m + '" selected>' + m + '</option>';
                                                    } else {
                                                        activityTemplate += '<option value="' + m + '">' + m + '</option>';
                                                    }
                                                }
                                            }
                                            activityTemplate += '</select>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="row">';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Purpose</label>';
                                            activityTemplate += '<div class="col-md-7">';
                                            activityTemplate += '<select class="form-control" id="id_purpose">';
                                            for (var i = 0; i < purposeData.length; i++) {
                                                var id = purposeData[i].id;
                                                var purpose = purposeData[i].purpose;
                                                activityTemplate += '<option value="' + id + '">' + purpose + '</option>';
                                            }
                                            activityTemplate += '</select>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="row">';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Owner</label>';
                                            activityTemplate += '<div class="col-md-7">';

                                            activityTemplate += '<select class="form-control" id="id_owner" onchange="assignedToList(this.value)">';
                                            activityTemplate += '<option>None</option>';
                                            for (var i = 0; i < datateamLead.length; i++) {
                                                var userId = datateamLead[i].user.id;
                                                var firstName = datateamLead[i].user.firstName;
                                                var lastName = datateamLead[i].user.lastName;
                                                var fullName = firstName + ' ' + lastName;
                                                activityTemplate += '<option value="' + userId + '">' + fullName + '</option>';
                                            }

                                            activityTemplate += '</select>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Assigned To</label>';
                                            activityTemplate += '<div class="col-md-7">';
                                            activityTemplate += '<select class="form-control" id="id_assigned">';
//                                            activityTemplate += '<select class="form-control" id="id_assigned" multiple="multiple">'; // multiple selection added by jyoti 13-july-2017, purpose - to add visits for multiple users
                                            activityTemplate += '<option>None</option>';

                                            activityTemplate += '</select>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="row">';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Status</label>';
                                            activityTemplate += '<div class="col-md-7">';
                                            activityTemplate += '<select class="form-control" id="id_status" onchange="outcomeForAddActivity()">';
                                            activityTemplate += '<option>Select</option>';
                                            activityTemplate += '<option value="2">Completed</option>';
                                            activityTemplate += '<option value="0" selected>Pending</option>';
                                            activityTemplate += '<option value="1">In-Progress</option>';
                                            activityTemplate += '<option value="3">Cancelled</option>';
                                            activityTemplate += '</select>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="col-md-6 form-group">';
                                            activityTemplate += '<label class="col-md-5 control-label">Outcome</label>';
                                            activityTemplate += '<div class="col-md-7">';
                                            activityTemplate += '<select class="form-control" id="id_outcome">';
                                            activityTemplate += '<option value="0" disabled>None</option>';
                                            activityTemplate += '</select>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="row">';
                                            //                                            activityTemplate += '<div class="form-group" id="dscp">';
                                            activityTemplate += '<div class="col-md-12 form-group" id="dscp">';
                                            activityTemplate += '<label class="col-md-3 control-label">Description</label>';
                                            activityTemplate += '<div class="col-md-9">';
                                            activityTemplate += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_description"></textarea>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<span id="id_outcomedescriptiontextbox" style="display:none;">';
                                            activityTemplate += '<div class="row">';
                                            activityTemplate += '<div class="col-md-12 form-group" id="dscp">';
                                            activityTemplate += '<label class="col-md-3 control-label">Outcome Description</label>';
                                            activityTemplate += '<div class="col-md-9">';
                                            activityTemplate += '<textarea class="form-control" rows="3" placeholder="Enter text" id="id_outcomedescription"></textarea>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</span>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '<div class="modal-footer">';
                                            activityTemplate += '<button type="submit" class="btn btn-info" onclick="createActivityInCustomer();return false;">Save</button>';
                                            activityTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</form>';
                                            activityTemplate += '</div>';
                                            activityTemplate += '</div>';
                                            $('.cls_addActivityInCustomer').html(activityTemplate);
                                            $('#pleaseWaitDialog').modal('hide');
                                            $('#id_createactivity').slimScroll({
                                                size: '7px',
                                                color: '#bbbbbb',
                                                opacity: .9,
                                                position: false ? 'left' : 'right',
                                                height: 420,
                                                allowPageScroll: false,
                                                disableFadeOut: false
                                            });
                                            //                                            App.init();
                                            $('.date-picker').datepicker({
                                                rtl: App.isRTL(),
                                                autoclose: true
                                            });
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
function customerContacts(customerId) {
    $('#pleaseWaitDialog').modal('show');
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
            $('#id_contact').html('');
            if (data.errorCode == '0000') {
                var contactData = data.result;
                var contactTemplate = '';
                contactTemplate = '<option>None</option>';
                for (var i = 0; i < contactData.length; i++) {
                    var contactId = contactData[i].id;
                    var firstName = contactData[i].firstName;
                    var middleName = contactData[i].middleName;
                    var lastName = contactData[i].lastName;
                    var fullName = firstName + ' ' + middleName + ' ' + lastName;
                    contactTemplate += '<option value="' + contactId + '">' + fullName + '</option>';
                }
                $('#id_contact').append(contactTemplate);
                $('#id_contactEdit').append(contactTemplate);
                $('#pleaseWaitDialog').modal('hide');
            } else {
                $('#pleaseWaitDialog').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function customerList() {
    $('#pleaseWaitDialog').modal('show');
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
            if (data.errorCode == '0000') {
                var customerData = data.result;
                for (var i = 0; i < customerData.length; i++) {
                    var customerId = customerData[i].id;
                    var customerPrintAs = customerData[i].customerPrintas;
                    var customerLocation = customerData[i].customerLocation;
                    var customerObj = {
                        "customerId": customerId,
                        "value": customerPrintAs + "-" + customerLocation
                    }
                    customer.push(customerObj);
                }
                $('#pleaseWaitDialog').modal('hide');
            } else {
                $('#pleaseWaitDialog').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function createActivityInCustomer() {
    var customer = document.getElementById("id_hidden").value.trim();
    var contact = document.getElementById("id_contact").value.trim();
    var activity = document.getElementById("id_activity").value.trim();
    var dates = document.getElementById("id_date").value.trim();
    var startHr = document.getElementById("activity_hr").value.trim();
    if (startHr == "HH") {
        fieldSenseTosterError("Start Time sould be selected", true);
        return false;
    }
    var startMin = document.getElementById("activity_min").value.trim();
    if (startMin == "MM") {
        fieldSenseTosterError("Start Time sould be selected", true);
        return false;
    }
    var endtHr = document.getElementById("activity_endHr").value.trim();
    if (endtHr == "HH") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    var endMin = document.getElementById("activity_endMin").value.trim();
    if (endMin == "MM") {
        fieldSenseTosterError("End Time should be selected", true);
        return false;
    }
    if (parseInt(startHr) > parseInt(endtHr) || (startHr == endtHr && parseInt(startMin) > parseInt(endMin))) {
        fieldSenseTosterError("Start Time should be less than End Time", true);
        return false;
    }
    var purpose = document.getElementById("id_purpose").value.trim();
    //by nikhil :- for clevertap
    var purposeText = document.getElementById("id_purpose");
  console.log("## "+purposeText.options[purposeText.selectedIndex].text); 
  var purposeTextVal = purposeText.options[purposeText.selectedIndex].text;
  console.log("purposeTextVal "+purposeTextVal);
  //ended by nikhil
    var owner = document.getElementById("id_owner").value.trim();
    var assignedTo = document.getElementById("id_assigned").value.trim();  // Commented by jyoti
//    // Added by jyoti, for adding visits to multiple users
//    var assignedToList = new Array();
//    assignedToList = $('#id_assigned').val();    // multi value selection
//    // Ended by jyoti
    var status = document.getElementById("id_status").value.trim();
    //by nikhil:- for clevertap
    var statustext =document.getElementById("id_status");
    var statusTextVal =statustext.options[statustext.selectedIndex].text;
    console.log("statusTextVal "+statusTextVal);
    //ended by nikhil
    var outcome = document.getElementById("id_outcome").value;
    var description = document.getElementById("id_description").value.trim();
    var dateSplit = dates.split("-");
    var date = dateSplit[0];
    var month = dateSplit[1];
    var year = dateSplit[2];
    var startDate = convertLocalDateToServerDate(year, month - 1, date, startHr, startMin);
    var adate = startDate.getFullYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDate() + " " + startDate.getHours() + ":" + startDate.getMinutes() + ':00';
    var endtHours = endtHr + ":" + endMin;
    var endDTime;
    //if (endtHours.trim() == "HH:MM") {
    //endDTime = adate;
    // } else {
    endDTime = convertLocalDateToServerDate(year, month - 1, date, endtHr, endMin);
    endDTime = endDTime.getFullYear() + "-" + (endDTime.getMonth() + 1) + "-" + endDTime.getDate() + " " + endDTime.getHours() + ":" + endDTime.getMinutes() + ':00';
    //}
    if (customer === 'Val') {
        fieldSenseTosterError("Customer Name cannot be blank", true);
        return false;
    }
    if (contact.trim() == "" || contact.trim() == "None") {
        contact = 0;
    }
//    if (activity.trim().length == 0) {
//        fieldSenseTosterError("Title cannot be blank", true);
//        return false;
//    }
    if (dates.trim().length == 0) {
        fieldSenseTosterError("Date cannot be empty", true);
        return false;
    }
    if (purpose === 'Select') {
        fieldSenseTosterError("Purpose cannot be empty", true);
        return false;
    }
    if (owner === 'None') {
        fieldSenseTosterError("Please select proper owner", true);
        return false;
    }
    if (assignedTo === 'None') {
//    if (assignedToList === 'None') { // var name changed by jyoti
        fieldSenseTosterError("Please select proper assigned to", true);
        return false;
    }
    if (status === 'Select') {
        fieldSenseTosterError("Please select proper status", true);
        return false;
    }
    var outcomeDescription = document.getElementById("id_outcomedescription").value.trim();
    if (outcomeDescription.trim().length != 0) {
        if (outcomeDescription.trim().length > 2000) {
            fieldSenseTosterError("Outcome Description can not be more than 2000 character", true);
            return false;
        }
    }
    $('#pleaseWaitDialog').modal('show');
    var activityObject = {
        "title": activity,
        "sdateTime": adate,
        "sendTime": endDTime,
        "customer": {
            "id": customer
        },
        "customerContact": {
            "id": contact
        },
        "owner": {
            "id": owner
        },
        "assignedTo": {
            "id": assignedTo // commented by jyoti, for add visit to multiple users 
        },
        "description": description,
        "type": 0,
        "purpose": {
            "id": purpose
        },
        "outcomes": {
            "id": outcome
        },
        "status": status,
        "createdBy": {
            "id": loginUserId
        },
        "outcomeDescription": outcomeDescription,
//        "assignedToList": assignedToList // added by jyoti, for add visit to multiple users 
    }
    var jsonData = JSON.stringify(activityObject);
    $.ajax({
        type: "POST",
//        url: fieldSenseURL + "/appointment/create/multipleUsers", //url modified by jyoti
        url: fieldSenseURL + "/appointment/create",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var activityData = data.result;
            if (data.errorCode == '0000') {
                $(".cls_addActivityInCustomer").modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
                appoinmentDetails(customer, ""); // added by jyoti
                // Commented by jyoti
//                var activityShowTemplate = '';
//                if (activityData.length != 0) {
//                    var activity = activityData.title;
//                     var purpose=activityData.purpose.purpose;
//                        if(activity==null || activity=="")
//                        {
//                            activity=purpose;
//                        }
//                    var appointmentId=activityData.id;
//                 //   var
//                    var activityOutCome = activityData.outcomes.id;
//                    var activityDateTime = activityData.sdateTime
//                    activityDateTime = activityDateTime.split(' ');
//                    var activityDate = activityDateTime[0];
//                    activityDate = activityDate.split('-');
//                    var activityTime = activityDateTime[1];
//                    activityTime = activityTime.split(':');
//                    var activityDateJs = convertServerDateToLocalDate(activityDate[0], activityDate[1] - 1, activityDate[2], activityTime[0], activityTime[1]);
//                    activityDateTime = activityDateJs.getFullYear() + "-" + (activityDateJs.getMonth() + 1) + "-" + activityDateJs.getDate() + " " + activityDateJs.getHours() + ":" + activityDateJs.getMinutes() + ":00.0";
//                    activityDateTime = fieldSenseseForJSDate(activityDateTime);
//                    var activityDescription = activityData.description;
//                    var activityStatusClass = '';
//                    if (activityOutCome == 0) {
//                        activityStatusClass = 'form-group';
//                    } else if (activityOutCome == 1) {
//                        activityStatusClass = 'form-group success';
//                    } else if (activityOutCome == 2) {
//                        activityStatusClass = 'form-group failed';
//                    }
//                    activityShowTemplate += '<div class="form-body">';
//                    activityShowTemplate += '<div class="' + activityStatusClass + '">';
//                  //  activityShowTemplate += '<label class="col-md-12 time-label">' + activityDateTime + '</label>';
//                    //
//                     activityShowTemplate += '<label class="col-md-2 time-label">' + activityDateTime + '</label>';
//                        activityShowTemplate += '<div class="col-md-1">';
//                         activityShowTemplate +='<button type="button" class="btn btn-default btn-xs tooltips"  data-placement="bottom" data-toggle="modal" href="#responsive4" title="Edit" onclick="editVisitTemplate(\'' + appointmentId + '\',\'' + "" + '\',\'' + "" + '\');return false;"><i class="fa fa-edit"></i></button>';
//                    activityShowTemplate +='</div>'
//                    activityShowTemplate += '<label class="col-md-12 title-label">' + activity + '</label>';
//                    activityShowTemplate += '<label class="col-md-12 dsc-label">' + activityDescription + '</label>';
//                 //  activityShowTemplate +='<button type="button" class="btn btn-default btn-xs tooltips"  data-placement="bottom" data-toggle="modal" href="#responsive4" title="Edit" onclick="editVisitTemplate(\'' + appointmentId + '\',\'' +" " + '\',\'' +" " + '\');return false;"><i class="fa fa-edit"></i></button>';
//                    activityShowTemplate += '</div>';
//                    activityShowTemplate += '</div>';
//                } else {
//                    activityShowTemplate += ' No activites for user ';
//                }
//                $('#id_toShowActivityInCustomer').prepend(activityShowTemplate);
                $('#pleaseWaitDialog').modal('hide');
                 clevertap.event.push("Add Visit", {
//                    "UserId":loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source":"Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                    "Purpose": purposeTextVal,
                    "Stsatus": statusTextVal,
                    //"Created On":data.result.createdOnString,
                });
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function outcomeForAddActivity() {
    var status = document.getElementById("id_status").value.trim();
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/activityOutcome",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken},
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (dataOutCome) {
            if (dataOutCome.errorCode == '0000') {
                var outData = dataOutCome.result;
                var activityTemplate = '';
                if (status == 2) {
                    for (var i = 0; i < outData.length; i++) {
                        var outId = outData[i].id;
                        var out = outData[i].outcome;
                        activityTemplate += '<option value="' + outId + '">' + out + '</option>';
                    }
                } else {
                    activityTemplate += '<option value="0">None</option>';
                }
                if (status == 2 || status == 3) {
                    $('#id_outcomedescriptiontextbox').css("display", "block");
                    $('#id_createactivity').slimscroll({
                        scrollBy: '420px'
                    });
                } else {
                    $('#id_outcomedescriptiontextbox').css("display", "none");
                }
                $('#id_outcome').html(activityTemplate);
            }
            $('#pleaseWaitDialog').modal('hide');
        }
    });
}

function editCustomerPageTemplate(customerId, customerName) {
    var url = '';
    if (role == 5) {
        url = "customers.html";
    } else if (role == 1 || role == 3 || role == 6 || role == 8) {
        url = "adminCustomer.html";
    }
    var editPageTemplate = '';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-12">';
    editPageTemplate += '<ul class="page-breadcrumb breadcrumb">';
    editPageTemplate += '<li>';
    editPageTemplate += '<i class="fa fa-user"/>';
    editPageTemplate += '<a href="">Customers</a>';
    editPageTemplate += '<i class="fa fa-angle-right"/>';
    editPageTemplate += '</li>';
    editPageTemplate += '<li>';
    editPageTemplate += '<i class="fa fa-edit"/>';
    editPageTemplate += '<a href="#" onclick="editCustomerDetails(\'' + customerId + '\',\'' + customerName + '\')">Edit Customer</a>';
    editPageTemplate += '</li>';
    editPageTemplate += '</ul>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="row" id="customer">';
    editPageTemplate += '<div class="col-md-12">';
    editPageTemplate += '<div class="portlet-body">';
    editPageTemplate += '<ul class="nav nav-tabs">';
    editPageTemplate += '<li class="active">';
    editPageTemplate += '<a href="#tab_cust1" data-toggle="tab" onclick="editCustomerDetails(\'' + customerId + '\',\'' + customerName + '\');return false;">Profile</a>';
    editPageTemplate += '</li>';
    editPageTemplate += '<li class="">';
    editPageTemplate += '<a href="#tab_cust2" data-toggle="tab" style="display:block;" id="Contacts" onclick="contactDetails(\'' + customerId + '\',\'' + customerName + '\')">Contact</a>';
    editPageTemplate += '</li>';
    editPageTemplate += '<li class="">';
    editPageTemplate += '<a href="#tab_cust3" data-toggle="tab" style="display:block;" id="Notes" onclick="notesDetails(\'' + customerId + '\',\'' + customerName + '\')">Notes</a>';
    editPageTemplate += '</li>';
    editPageTemplate += '<li class="">';
    editPageTemplate += '<a href="#tab_cust4" data-toggle="tab" style="display:block;" id="Activity" onclick="appoinmentDetails(\'' + customerId + '\',\'' + customerName + '\')">Visit</a>';
    editPageTemplate += '</li>';
    editPageTemplate += '</ul>';
    editPageTemplate += '<div class="tab-content">';
    editPageTemplate += '<div class="tab-pane fade active in" id="tab_cust1">';
    editPageTemplate += '<span id="id_editCustomer">';
    editPageTemplate += '</span>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="tab-pane fade" id="tab_cust2">';
    editPageTemplate += '<div class="portlet">';
    editPageTemplate += '<div class="portlet-title">';
    editPageTemplate += '<div class="caption">';
    editPageTemplate += '<i class="fa fa-list"></i>Contacts';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="actions">';
    editPageTemplate += '<a data-toggle="modal" href="#responsive" class="btn btn-info" ><i class="fa fa-pencil"></i> Add</a>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="portlet-body">';
    editPageTemplate += '<div class="table-responsive">';
    editPageTemplate += '<table class="table table-striped table-bordered table-hover">';
    editPageTemplate += '<thead>';
    editPageTemplate += '<tr>';
    editPageTemplate += '<th>';
    editPageTemplate += 'Name';
    editPageTemplate += '</th>';
    editPageTemplate += '<th>';
    editPageTemplate += 'Mobile';
    editPageTemplate += '</th>';
    editPageTemplate += '<th>';
    editPageTemplate += 'Email';
    editPageTemplate += '</th>';
    editPageTemplate += '<th>';
    editPageTemplate += '</th>';
    editPageTemplate += '</tr>';
    editPageTemplate += '</thead>';
    editPageTemplate += '<tbody id="id_viewContactTemplate">';
    editPageTemplate += '</tbody>';
    editPageTemplate += '</table>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="tab-pane fade" id="tab_cust3">';
    editPageTemplate += '<form class="form-horizontal" role="form">';
    editPageTemplate += '<div class="buttons">';
    editPageTemplate += '<button type="button" class="btn btn-sm btn-info" data-toggle="modal" href="#responsive3"><i class="fa fa-plus"></i> Add New</button>';
    editPageTemplate += '</div>';
    editPageTemplate += '<span class="cls_viewNotesOneCustomer"></span>';
    editPageTemplate += '</form>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div id="responsive3" class="modal fade cls_notes" tabindex="-1" aria-hidden="true">';
    editPageTemplate += '<div class="modal-dialog">';
    editPageTemplate += '<div class="modal-content">';
    editPageTemplate += '<div class="modal-header">';
    editPageTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    editPageTemplate += '<h4 class="modal-title">Add Notes</h4>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="modal-body">';
    editPageTemplate += '<div class="form-body">';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-12 form-group">';
    editPageTemplate += '<textarea class="form-control" rows="3" placeholder="Enter Notes" tabindex="5" id="id_notes"></textarea>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="modal-footer">';
    editPageTemplate += '<span id="id_notesSaveInEdit">';
    editPageTemplate += '<button class="btn btn-info" data-dismiss="modal" onClick="createNotes()" tabindex="17" type="button">Save</button>';
    editPageTemplate += '</span>';
    editPageTemplate += '<button class="btn btn-default cls_btnspace" data-dismiss="modal" tabindex="18" type="button">Cancel</button>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div id="responsive5" class="modal fade editNotesTemplate" tabindex="-1" aria-hidden="true">';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="tab-pane fade" id="tab_cust4">';
    editPageTemplate += '<form class="form-horizontal" role="form">';
    editPageTemplate += '<div class="buttons">';
    editPageTemplate += '<button class="btn btn-sm btn-info" data-toggle="modal" href="#responsive4" type="button" onClick="addActivityInCustomerTemplate(\'' + customerId + '\',\'' + customerName + '\')">';
    editPageTemplate += '<i class="fa fa-plus"/> Add New</button>';
    editPageTemplate += '</div>';
    editPageTemplate += '<span id="id_toShowActivityInCustomer"/>';
    editPageTemplate += '</form>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div id="responsive4" class="modal fade cls_addActivityInCustomer" tabindex="-1" role="basic" aria-hidden="true"> ';
    editPageTemplate += '</div>';
    editPageTemplate += '<div id="responsive2" class="modal fade id_editAddcontactTemplate" tabindex="-1" role="basic" aria-hidden="true"">';
    editPageTemplate += '</div>';
    editPageTemplate += '<div id="responsive" class="modal fade cls_addContactInEditTemplate" tabindex="-1" aria-hidden="true">';
    editPageTemplate += '<div class="modal-dialog modal-wide">';
    editPageTemplate += '<div class="modal-content">';
    editPageTemplate += '<div class="modal-header">';
    editPageTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    editPageTemplate += '<h4 class="modal-title">Add Contact</h4>';
    editPageTemplate += '</div>';
    editPageTemplate += '<form class="form-horizontal" role="form">';
    editPageTemplate += '<div class="modal-body" id="act">';
    editPageTemplate += '<div class="scroller" style="max-height:410px;height:410px;" data-always-visible="1" data-rail-visible1="1">';
    editPageTemplate += '<div class="form-body">';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">First Name</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="fname" class="form-control" placeholder="Enter First Name" tabindex="1" id="id_firstname">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Mobile1</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Mobile1" class="form-control cls_mobile1" placeholder="Enter Mobile1" id="mask_mobile" tabindex="9">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Middle Name</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="mname" class="form-control" placeholder="Enter Middle Name" tabindex="2" id="id_middlename">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Mobile2</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Mobile2" class="form-control cls_mobile2" placeholder="Enter Mobile2" id="mask_mobile1" tabindex="10">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Last Name</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="lname" class="form-control" placeholder="Enter Last Name" tabindex="3" id="id_lastname">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Phone1</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Phone2" class="form-control cls_phonec1" placeholder="Enter Phone1" id="mask_phone2" tabindex="11">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Designation</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Designation" class="form-control" placeholder="Enter Designation" tabindex="4" id="id_designation">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Phone2</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Phone3" class="form-control cls_phonec2" placeholder="Enter Phone2" id="mask_phone3" tabindex="12">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Reports To</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Reports To" class="form-control" placeholder="Enter Reports To" tabindex="5" id="id_reportsTo">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Phone3</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Phone3" class="form-control cls_phonec3" placeholder="Enter Phone3" id="mask_phone3" tabindex="12">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Fax1</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Fax" class="form-control cls_faxc1" placeholder="Enter Fax" id="mask_fax" tabindex="13">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Fax2</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Fax" class="form-control cls_faxc2" placeholder="Enter Fax" id="mask_fax" tabindex="13">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Assistant Name</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Assistant Name" class="form-control" placeholder="Enter Assistant Name" tabindex="6" id="id_assistancenm">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Email1</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Email1" class="form-control cls_emailc1" placeholder="Enter Email1" id="inputEmail1" tabindex="14">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Date of Birth</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input class="form-control date-picker" data-date-format="dd-mm-yyyy" size="16" type="text" value="" placeholder="Select date" tabindex="7" id="id_birthdate"/>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Email2</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Email2" class="form-control cls_emailc2" placeholder="Enter Email2" id="inputEmail2" tabindex="15">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="row">';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Spouse Name</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input type="Spouse Name" class="form-control" placeholder="Enter Spouse Name" tabindex="8" id="id_spousenm">';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="col-md-6 form-group">';
    editPageTemplate += '<label class="col-md-5 control-label">Anniversary Date</label>';
    editPageTemplate += '<div class="col-md-7">';
    editPageTemplate += '<input class="form-control date-picker" data-date-format="dd-mm-yyyy" size="16" type="text" value="" placeholder="Select date" tabindex="16" id="id_anniversarydate"/>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '<div class="modal-footer">';
    editPageTemplate += '<span class="custId"><button type="submit" class="btn btn-info" onClick="createContact();return false;">Save</button></span>';
    editPageTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default" tabindex="18" onclick="clearIds()">Cancel</button>';
    editPageTemplate += '</div>';
    editPageTemplate += '</form>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    editPageTemplate += '</div>';
    $('#id_editCustomerPage').html(editPageTemplate);
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });
}
function editNotes(customerId, noteId) {
    var description = document.getElementById("id_notesEdit").value.trim();
    if (description.trim().length == 0) {
        fieldSenseTosterError("Notes cannot be empty", true);
        return false;
    }
    description = description.trim();
    var notesObject = {
        "id": noteId,
        "userId": {
            "id": loginUserId
        },
        "customerId": customerId,
        "description": description
    }
    var jsonData = JSON.stringify(notesObject);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/notes",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        success: function (data) {
            var notesData = data.result;
            if (data.errorCode == '0000') {
                $.ajax({
                    type: "GET",
                    url: fieldSenseURL + "/notes/" + customerId,
                    contentType: "application/json; charset=utf-8",
                    headers: {
                        "userToken": userToken
                    },
                    crossDomain: false,
                    cache: false,
                    dataType: 'json',
                    asyn: true,
                    success: function (notedatas) {
                        if (notedatas.errorCode == '0000') {
                            var noteTemplate = '';
                            var notesData = notedatas.result;
                            for (var i = 0; i < notesData.length; i++) {
                                var id = notesData[i].id;
                                var note = notesData[i].description;
                                var firstnm = notesData[i].userId.firstName;
                                var lastnm = notesData[i].userId.lastName;
                                var createdOn = notesData[i].createdOn;
                                createdOn = fieldSenseDateTimeForNotes(createdOn);
                                var fullNm = firstnm + ' ' + lastnm;
                                var id_comments = 'comments' + id;
                                noteTemplate += '<div class="form-group comm">';
                                noteTemplate += '<div class="col-md-12">';
                                noteTemplate += '<label id="' + id_comments + '">' + note + '</label>';
                                noteTemplate += '</div>';
                                noteTemplate += '<label class="col-md-12"> <span class="time-note">Added by ' + fullNm + ' on ' + createdOn;
                                noteTemplate += '<button type="button" class="btn btn-xs btn-default" data-toggle="modal" href="#responsive5" onclick="editNotesTemplate(\'' + customerId + '\',\'' + id + '\')"><i class="fa fa-edit"></i></button></label>';
                                noteTemplate += '</div>';
                            }
                            $('.cls_viewNotesOneCustomer').html(noteTemplate);
                        }
                    }
                });
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
function geoCodeMapForCustomer() {
    var map;
    var markers = [];
    function intializieMap() {
        var mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(18.505893, 73.840760),
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        map = new google.maps.Map(document.getElementById('gmap_marker_customer'), mapOptions);
        google.maps.event.addListener(map, 'rightclick', function (event) {
            addMapMarker(event.latLng);
        });
    }
    intializieMap();
    // Sets the map on all markers in the array.
    function setAllMap(map) {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(map);
        }
    }
    // Add a marker to the map and push to the array.
    function addMapMarker(location) {
        setAllMap(null);
        var marker = new google.maps.Marker({
            position: location,
            map: map,
            draggable: true, //set marker draggable
            animation: google.maps.Animation.DROP //bounce animation
        });
        google.maps.event.addListener(
                marker,
                'drag',
                function () {
                    var point = marker.getPosition();
                    lat = point.lat();
                    lng = point.lng();
                    var latlong = lat + ', ' + lng;
                    document.getElementById("id_cuslatlng").value = latlong;
                }
        );
        lat = location.lat();
        lng = location.lng();
        var latlong = lat + ', ' + lng;
        document.getElementById("id_cuslatlng").value = latlong;
        markers.push(marker);
    }
    var address = document.getElementById("id_address").value;
    if (address.trim().length != 0) {
        address = document.getElementById("id_address").value;
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({
            'address': address
        }, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                map.setCenter(results[0].geometry.location);
                addMapMarker(results[0].geometry.location);
            } else {
                alert("Lat and long cannot be found.");
            }
        });
    }
    $("#id_address").change(function () {
        var address = document.getElementById("id_address").value;
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({
            'address': address
        }, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                map.setCenter(results[0].geometry.location);
                addMapMarker(results[0].geometry.location);
            } else {
                alert("Lat and long cannot be found.");
            }
        });
    });
}
function editNotesTemplate(customerId, noteId) {
    var id_comments = 'comments' + noteId;
    var description = document.getElementById(id_comments).innerHTML;
    var editNotesTemplate = '';
    editNotesTemplate += '<div class="modal-dialog">';
    editNotesTemplate += '<div class="modal-content">';
    editNotesTemplate += '<div class="modal-header">';
    editNotesTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    editNotesTemplate += '<h4 class="modal-title">Edit Notes</h4>';
    editNotesTemplate += '</div>';
    editNotesTemplate += '<div class="modal-body">';
    editNotesTemplate += '<div class="form-body">';
    editNotesTemplate += '<div class="row">';
    editNotesTemplate += '<div class="col-md-12 form-group">';
    editNotesTemplate += '<textarea class="form-control" rows="3" placeholder="Enter Notes" tabindex="5" id="id_notesEdit">' + description + '</textarea>';
    editNotesTemplate += '</div>';
    editNotesTemplate += '</div>';
    editNotesTemplate += '</div>';
    editNotesTemplate += '</div>';
    editNotesTemplate += '<div class="modal-footer">';
    editNotesTemplate += '<span id="id_notesSaveInEdit">';
    editNotesTemplate += '<button class="btn btn-info" data-dismiss="modal" onClick="editNotes(\'' + customerId + '\',\'' + noteId + '\')" tabindex="17" type="button">Save</button>';
    editNotesTemplate += '</span>';
    editNotesTemplate += '<button class="btn btn-default cls_btnspace" data-dismiss="modal" tabindex="18" type="button">Cancel</button>';
    editNotesTemplate += '</div>';
    editNotesTemplate += '</div>';
    editNotesTemplate += '</div>';
    $('.editNotesTemplate').html(editNotesTemplate);
}
function assignedToList(teamLeadId) {
    $('#pleaseWaitDialog').modal('show');
    $('#id_assigned').html('');
    var activityTemplate = '';
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/userPositions/" + teamLeadId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (datateamMember) {
            if (datateamMember.errorCode == '0000') {
                datateamMember = datateamMember.result;
                var length = datateamMember.length;
                var id = $('#id_owner').val();
                var value = $("#id_owner option:selected").text();
                activityTemplate += '<option value="' + id + '">' + value + '</option>';
                for (var i = 0; i < length; i++) {
                    var userId = datateamMember[i].user.id;
                    if (teamLeadId != userId) {
                        var firstName = datateamMember[i].user.firstName;
                        var lastName = datateamMember[i].user.lastName;
                        var fullName = firstName + ' ' + lastName;
                        activityTemplate += '<option value="' + userId + '">' + fullName + '</option>';
                    }
                }
                $('#id_assigned').html(activityTemplate);
            }
            $('#pleaseWaitDialog').modal('hide');
        },
        error: function (xhr, status, error) {
            $('#pleaseWaitDialog').modal('hide');
            activityTemplate += '<option>None</option>';
            $('#id_assigned').html(activityTemplate);
        }
    });
}

function assignedToListEdit(teamLeadId) {
    $('#pleaseWaitDialog').modal('show');
    $('#id_assigned').html('');
    var editActivityTemplateHtml = '';
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/userPositions/" + teamLeadId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (datateamMember) {
            if (datateamMember.errorCode == '0000') {
                datateamMember = datateamMember.result;
                var length = datateamMember.length;
                var id = $('#id_owner').val();
                var value = $("#id_owner option:selected").text();
                editActivityTemplateHtml += '<option value="' + id + '">' + value + '</option>';
                for (var i = 0; i < length; i++) {
                    var userId = datateamMember[i].user.id;
                    if (teamLeadId != userId) {
                        var firstName = datateamMember[i].user.firstName;
                        var lastName = datateamMember[i].user.lastName;
                        var fullName = firstName + ' ' + lastName;
                        editActivityTemplateHtml += '<option value="' + userId + '">' + fullName + '</option>';
                    }
                }
                $('#id_assigned').html(editActivityTemplateHtml);
            }
            $('#pleaseWaitDialog').modal('hide');
        },
        error: function (xhr, status, error) {
            $('#pleaseWaitDialog').modal('hide');
            editActivityTemplateHtml += '<option>None</option>';
            $('#id_assigned').html(editActivityTemplateHtml);
        }
    });
}
function downloadSampleCustomersFile() {
    var url = imageURLPath + "customers.csv";
    var temp = '<a href="' + url + '" style="margin-right: 775px;" download>Click here to download sample file</a>';
    $('#id_customerTemplate').html(temp);
    var url = imageURLPath + "contacts.csv";
    var temp1 = '<a href="' + url + '" style="margin-right: 775px;" download>Click here to download sample file</a>';
    $('#id_contactTemplate').html(temp1);
}

function getActiveTerritories() {
    
    var territory_url = fieldSenseURL + "/territoryCategory/active/territory/user/" + loginUserId;
    if (role == 1 || role == 3 || role == 6 || role == 8) {
        territory_url = fieldSenseURL + "/territoryCategory/active/territory";
    }
    
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: territory_url,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                var territoryData = data.result;
                for (var i = 0; i < territoryData.length; i++) {
                    if (territoryData[i].parentCategory == 0) {
                        if (territoryData[i].hasChild == 0) {
                            parentTerritoryCatagories.push({"id": territoryData[i].id, "categoryName": territoryData[i].categoryName, "categoryPositionCsv": territoryData[i].categoryPositionCsv});
                        } else {
                            parentTerritoryCatagories.push({"id": territoryData[i].id, "categoryName": territoryData[i].categoryName, "categoryPositionCsv": territoryData[i].categoryPositionCsv, "list": [{"id": territoryData[i].id, "categoryName": "unknown"}]});
                        }
                    } else {
                        fullTerriCategories.push({"parentCat": territoryData[i].parentCategory, "id": territoryData[i].id, "categoryName": territoryData[i].categoryName, "hasChild": territoryData[i].hasChild, "categoryPositionCsv": territoryData[i].categoryPositionCsv});
                    }
                }
                $('#drilldown1').drilldownSelect({appendValue: false, data: parentTerritoryCatagories});
                $('#pleaseWaitDialog').modal('hide');
            } else {
                $('#pleaseWaitDialog').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}

function getActiveIndustries() {

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/industryCategory/active",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                var industryData = data.result;
                var activeIndustryTemplate = "<option> Select </option>";
                for (var i = 0; i < industryData.length; i++) {
                    activeIndustryTemplate += "<option>" + industryData[i].categoryName + "</option>";
                }
                $("#id_industry").html(activeIndustryTemplate);
                ;
                //$('#pleaseWaitDialog').modal('hide');
            } else {
                //$('#pleaseWaitDialog').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
        }
    });
}
