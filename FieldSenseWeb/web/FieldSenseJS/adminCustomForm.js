var intervalAdminCustomer = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            loadLeftNav();
            getMasterData();
            setBlankForm();
            window.clearTimeout(intervalAdminCustomer);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);

var tableData = [];
var deletedFields = [];
var masterDataList = []; //Get names of list from master data
var userDefinedList = [];
var systemDefinedList = [];
//added by nikhil
var loginUserIdcookie = fieldSenseGetCookie("loginUserIdcookie");
var loginUserNamecookie = fieldSenseGetCookie("loginUserNamecookie");
var accountIdcookie = fieldSenseGetCookie("accountIdcookie");
var accountNamecookie = fieldSenseGetCookie("accountNamecookie");
var UserRolecookie = fieldSenseGetCookie("UserRolecookie");
//ended by nikhil

function loadLeftNav() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/adminCustomForms/all",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                var formid = 1; // get this from return data
                var addformName = 'SampleName';
                var formList = data.result;
                var templ = "";
                for (i = 0; i < formList.length; i++)
                {
                    templ += '<li id="leftnav_form_' + formList[i].id + '" class="leftnav_form"><a class="showSingle" onclick="editFormTemplate(' + formList[i].id + ')"> <span class="barrow"></span>';
                    templ += '<span class="membername" title="' + formList[i].formName + '">' + formList[i].formName + '</span></a></li>';
                }
                $("#formListMenu").html(templ);
            }
        }
    });

}

function editFormTemplate(id) {

    $('#pleaseWaitDialog').modal('show');
    var editFormTemplate = "";

    var myTable = $('#sample_21i').DataTable();
    myTable.clear(); // clear old table values

    $(".leftnav_form").removeClass("active");
    $("#leftnav_form_" + id).addClass("active");
    // This do be done in success

    // this all things to be done in success

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/adminCustomForms/" + id, //need to change this url
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            var formName = data.result.formName;
            var form_related_to = data.result.form_related_to;
            var form_trigerred_event = data.result.form_trigerred_event;
            var submit_caption = data.result.submit_caption;
            var active = data.result.active;
            var mobileOnly = data.result.mobileOnly;
            //var datatable=data.result.tableData;

            $("#addformName").val(formName); // form name
            $("#relatedTo").val(form_related_to);
            $("#form_trigger_event").val(form_trigerred_event);
            $("#SubmitCaption").val(submit_caption);
            if (active == 1) {
                $("#inlineCheckbox1").parent().attr('class', 'checked');
                $("#inlineCheckbox1").attr('checked', true);
            } else {
                $("#inlineCheckbox1").parent().removeClass('checked');
                $("#inlineCheckbox1").attr('checked', false);
            }
            if (mobileOnly == 1) {
                $("#inlineCheckbox2").parent().attr('class', 'checked');
                $("#inlineCheckbox2").attr('checked', true);
            } else {
                $("#inlineCheckbox2").parent().removeClass('checked');
                $("#inlineCheckbox2").attr('checked', false);
            }

            tableData = data.result.tableData; //To be assigned new value

            deletedFields = [];
            var mainArray = [];

            var add1 = [];
            for (i = 0; i < tableData.length; i++) {
                var field = tableData[i];
                add1 = [];
                add1.push("");
                add1.push(field.labelName);
                add1.push(field.fieldType);
                if (field.mandField === 0) {
                    add1.push(false);
                } else {
                    add1.push(true);
                }
                add1.push('<button title="Edit" data-placement="bottom" href="#form_responsive" onclick="editRow($(this))" data-toggle="modal" class="btn btn-default btn-xs tooltips" type="button"><i class="fa fa-edit"></i></button>&nbsp;<button title="Delete" data-placement="bottom" class="btn btn-default btn-xs tooltips " onclick="deleteRow($(this))" type="button"><i class="fa fa-trash-o"></i></button>&nbsp;<button title="Move Up" data-placement="bottom" class="btn btn-default btn-xs tooltips moveup" type="button"><i class="fa fa-angle-up"></i></button>&nbsp;<button title="Move Down" data-placement="bottom" class="btn btn-default btn-xs tooltips movedown" type="button"><i class="fa fa-angle-down"></i></button>');
                mainArray.push(add1);
            }

            myTable.rows.add(mainArray).draw();
            //$("#add_form_footer button:eq(0)" ).html("Update");
            //$("#add_form_footer button:eq(0)" ).attr("onclick","submitForm('update',"+id+")");
            //$("#add_form_footer button:eq(1)" ).html("Reset");
            //$("#add_form_footer button:eq(1)" ).attr("onclick","editFormTemplate("+id+")");
            //$("#add_form_footer button:eq(3)" ).show();
            //$("#add_form_footer button:eq(3)" ).attr("onclick","deleteForm("+id+")");
            var foootertmpl = '<button type="button" class="btn btn-info" style="margin-right:3px" onclick="submitForm(\'update\',' + id + ');">Update</button>';
            foootertmpl += '<button type="button" class="btn btn-default" style="margin-right:3px" onclick="editFormTemplate(' + id + ')">Reset</button>';
            foootertmpl += '<button type="button" class="btn btn-default" data-toggle="modal" href="#form_preview" onclick="checkPreview();">Preview</button>';
            foootertmpl += '<button type="button" class="btn btn-default fr" onclick="deleteForm(' + id + ')" >Delete Form</button>';
            $("#add_form_footer").html(foootertmpl);
            App.initUniform();
            App.fixContentHeight();
            jQuery('.tooltips').tooltip();
            $('#pleaseWaitDialog').modal('hide');
        }
    });
}

function addFieldRow(action, rowIndex) {
    //First get the value of all fields
    if (validateField() === false) {
        return false;
    }
    //validateField();
    var index = -1;
    if (action == 'update') {
        index = rowIndex;
    }
    var label = $("#comp_label").val().trim();
    var type = $('#comp_type').find(":selected").text().trim();

    var placeHolder = $("#comp_placeholder").val().trim();
    var isGeoEnable = $("#enable_geo_check").is(':checked');
    //var listType="";
    var listIdFK = $('#Predefined_list').val();
    // var listName="";

    //   var dbvalues=$('#default_value').find(":selected").text();

    if (isGeoEnable == false) {
        isGeoEnable = 0;
    } else
    if (isGeoEnable == true) {
        isGeoEnable = 1;
    }
    var isEnable = $('input[name=optionsRadios]:checked', '#add_component').val();
    var isMandatory = $('input[name=optionsRadios1]:checked', '#add_component').val();
    console.log("isMandatory #" + isMandatory);
    var mand = false;
    if (isMandatory == 1 || isMandatory == "1") {
        mand = true;
        if (isEnable == 0 || isEnable == "0") {
            fieldSenseTosterError("Mandatory fields should be editable", true);
            return false;
        }
    }
    var options = $("#comp_values").val().trim();
    if (type === "Textbox" || type === "Textarea" || type === "Number" || type === "Date" || type === "Time" || type === "Location") // added location by jyoti, Feature #30909
        options = "";
    //   listType="";
    listIdFK = "";


    var myTable = $('#sample_21i').DataTable();
    if (type === "Predefined Lists") {
        options = "";
        placeHolder = "";

        //   listType=$('#list_type').find(":selected").text().trim();
        listIdFK = $('#Predefined_list').val();
        //   listName=$('#Predefined_list').find(":selected").text().trim();
    }

    // remove spaces and blank values from options
    var arr = options.split("\n");
    options = "";
    for (var i = 0; i < arr.length; i++) {
        if (arr[i].trim() != "")
            options += arr[i].trim() + "\n";
    }
    options = options.trim();

    /////// check duplicate option
    arr = options.split("\n");  //reuse arr only
    var sorted_arr = arr.sort(); // You can define the comparing function here. 
    // JS by default uses a crappy string compare.
    var results = [];

    for (var i = 0; i < arr.length - 1; i++) {
        if (sorted_arr[i + 1].trim() == sorted_arr[i].trim()) {
            results.push(sorted_arr[i]);
        }
    }


    if (results.length > 0) {
        alert("Values contains duplicate entries :" + results);
        return false;
    }

    /////// end check 


    if (action == 'update') {

        tableData[index].labelName = label;
        tableData[index].fieldType = type;
        tableData[index].placeHolder = placeHolder;
        tableData[index].geoTagEnable = isGeoEnable;
        tableData[index].editField = isEnable;
        tableData[index].mandField = isMandatory;
        tableData[index].option = options;
        //  tableData[index].listType=listType;
        //  tableData[index].listName=listName;
        tableData[index].listIdFK = listIdFK;
        //tableData.splice(index, 0, field);


        $('#sample_21i tr:nth-child(' + (index + 1) + ') td:nth-child(2)').html(label);
        $('#sample_21i tr:nth-child(' + (index + 1) + ') td:nth-child(3)').html(type);
        $('#sample_21i tr:nth-child(' + (index + 1) + ') td:nth-child(4)').html("" + mand);

        //clearAddComponentDet();//clear form for later use
        $('#form_responsive').modal('toggle');
        return;   // in case of update return from it
    }


    var field = {
        "fieldId": 0, // id 0 indicates new field is added
        "labelName": label,
        "fieldType": type,
        "placeHolder": placeHolder,
        "geoTagEnable": isGeoEnable,
        "editField": isEnable,
        "mandField": isMandatory,
        "option": options,
        //    "listType":listType,
        //    "listName":listName,
        "listIdFK": listIdFK
    };

    tableData.push(field);

    var add1 = [];
    add1.push("");
    add1.push(label);
    add1.push(type);
    add1.push(mand);
    add1.push('<button title="Edit" data-placement="bottom" href="#form_responsive" onclick="editRow($(this))" data-toggle="modal" class="btn btn-default btn-xs tooltips" type="button"><i class="fa fa-edit"></i></button>&nbsp;<button title="Delete" data-placement="bottom" class="btn btn-default btn-xs tooltips " onclick="deleteRow($(this))" type="button"><i class="fa fa-trash-o"></i></button>&nbsp;<button title="Move Up" data-placement="bottom" class="btn btn-default btn-xs tooltips moveup" type="button"><i class="fa fa-angle-up"></i></button>&nbsp;<button title="Move Down" data-placement="bottom" class="btn btn-default btn-xs tooltips movedown" type="button"><i class="fa fa-angle-down"></i></button>');

    myTable.row.add(add1).draw();
    App.initUniform();
    App.fixContentHeight();
    jQuery('.tooltips').tooltip();
    $('#form_responsive').modal('toggle');
    //clearAddComponentDet();//clear form for later use

}

function modifyOptionAddComp() {



    /*var type=$('#comp_type').find(":selected").text();
     if(type==="Dropdown" || type==="Radio" || type==="Checkboxes") {
     $("#addCompValueLabel").show();
     $("#addCompPlaceHolder").hide();
     $("#addCompPlaceHolder").val("");
     }else{
     $("#addCompValueLabel").hide();
     $("#addCompPlaceHolder").show();
     $("#addCompValueLabel").val("");
     }
     var htmlFooter='<button type="button" class="btn btn-info" onclick="addFieldRow(\'add\');">Add</button>';
     htmlFooter  +='<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
     $('#add_comp_footer').html(htmlFooter);
     $('.modal-title').html('Add Component');
     clearAddComponentDet();*/
    // new component 

    $('#form_responsive').html('');

    var addCompTmpl = '';
    addCompTmpl += '<div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> ';
    addCompTmpl += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addCompTmpl += '<h4 class="modal-title">Add Component</h4></div>';
    addCompTmpl += '<div class="modal-body"><div class="scroller" style="height:300px" data-always-visible="1" data-rail-visible1="1">';
    addCompTmpl += '<div class="row"> <div class="col-md-12"><form class="form-horizontal" role="form" id="add_component"><div class="form-body"> ';
    addCompTmpl += '<div class="form-group"> <label class="col-md-4 control-label">Label</label> <div class="col-md-8">';
    addCompTmpl += '<input type="text" id="comp_label" class="form-control" placeholder="Label">	</div> 	</div> ';
    addCompTmpl += '<div class="form-group"> <label class="col-md-4 control-label">Type</label> <div class="col-md-8"> ';
    addCompTmpl += '<select class="form-control" id="comp_type" onchange="resetOnSelectOfType()">';
    addCompTmpl += '<option>Textbox</option> <option>Textarea</option> <option>Number</option> <option>Date</option> ';
    addCompTmpl += '<option>Location</option>'; // added by jyoti, Feature #30909
    addCompTmpl += '<option>Dropdown</option><option>Predefined Lists</option> <option>Checkboxes</option> <option>Radio</option><option>Image</option><option>Time</option> ';
    addCompTmpl += '</select></div></div>';

//    //Added for user defined list
    addCompTmpl += '<div class="form-group" id=List_Display style="display:none"> <label class="col-md-4 control-label">Value Should be fetch from</label> <div class="col-md-8"> ';
    addCompTmpl += '<select class="form-control" id="list_type" onchange="resetOnSelectOfList()">';
    addCompTmpl += '<option>----select-----</option><option>System Defined List</option> <option>User Defined List</option>';
    addCompTmpl += '</select></div></div>';

    addCompTmpl += '<div class="form-group" id=showList style="display:none"> <label class="col-md-4 control-label">Lists names From Master data</label> <div class="col-md-8" ><select id=Predefined_list class="form-control"></select> </div></div>';

//    //

    addCompTmpl += '<div class="form-group" id="enable_geo_tagging" style="display:none">';
    addCompTmpl += '<label class="col-md-4 control-label">Enable Geo-tagging</label>';
    addCompTmpl += '<div class="col-md-8">';
    addCompTmpl += '<div class="checkbox-list">';
    addCompTmpl += '<label class="checkbox-inline">';
    addCompTmpl += '<input type="checkbox" id="enable_geo_check"></label>';
    addCompTmpl += '</div>';
    addCompTmpl += '</div>';
    addCompTmpl += '</div>';

    addCompTmpl += '<div class="form-group" id="addCompPlaceHolder"> <label class="col-md-4 control-label">Placeholder</label><div class="col-md-8">';
    addCompTmpl += '<input type="text" id="comp_placeholder" class="form-control" placeholder="Placeholder"></div></div> ';
    addCompTmpl += '<div class="form-group" id="addCompMandatory"><label class="col-md-4 control-label">Mandatory</label><div class="col-md-8"><div class="radio-list"> ';
    addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios1" id="optionsRadios27" value=1 checked> Yes </label>';
    addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios1" id="optionsRadios28" value=0 > No </label> ';
    addCompTmpl += '</div></div></div>';
    addCompTmpl += '<div class="form-group" id="addCompEditable"><label class="col-md-4 control-label">Editable</label><div class="col-md-8"><div class="radio-list"> ';
    addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios" id="optionsRadios25" value=1 checked> Yes </label>';
    addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios" id="optionsRadios26" value=0 > No </label> ';
    addCompTmpl += '</div></div></div>';
    addCompTmpl += '<div class="form-group" id="addCompValueLabel" style="display:none"> <label class="col-md-4 control-label">Values</label> <div class="col-md-8"> ';
    addCompTmpl += '<textarea class="form-control" id="comp_values" rows="3" placeholder="Enter Values separated by new line"></textarea>';
    addCompTmpl += '</div> </div> ';
    addCompTmpl += '</div></form></div></div></div></div>';
    addCompTmpl += '<div class="modal-footer" id ="add_comp_footer"> ';
    addCompTmpl += '<button type="button"  class="btn btn-info" onclick="addFieldRow(\'add\');">Add</button> ';
    addCompTmpl += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button> </div> ';
    addCompTmpl += '</div> </div> ';

    $('#form_responsive').html(addCompTmpl);

    //resetOnSelectOfType();
    App.fixContentHeight();
    App.initUniform();
    App.callHandleScrollers(); // new method added

}
//function for changes in page
// function resetOnSelectOfValue()
//{
//     var dbvalues=$('#default_value').find(":selected").text();
//      if(dbvalues==="Customer Name" || dbvalues==="Address") 
//      {
//          $("#comp_type").val("Dropdown");
//           $("#comp_type").prop("disabled", true);
//           $('#addCompValueLabel').hide();
//      }
//      if(dbvalues==="No Change")
//      {
//          $("#comp_type").val("1"); 
//          $("#comp_type").prop("disabled", false);
//      }
//}

//End of the function
//#11446
//Added for the the predefined list.
function resetOnSelectOfList() {
    // var listName=[];
    var addCompTmpl = '';
    var listValues = $('#list_type').find(":selected").text().trim();
    if (listValues == "----select-----")
    {
        $("#showList").hide();
    }
    if (listValues === "System Defined List") {
        $("#showList").show();
        addCompTmpl += '<option value="---select---">---select---</option>';
        for (var j = 0; j < masterDataList.systemDefined.length; j++) {
            addCompTmpl += "<option value='" + masterDataList.systemDefined[j].id + "'>" + masterDataList.systemDefined[j].listName + "</option>";
        }
        $('#Predefined_list').html(addCompTmpl);
    }
    else if (listValues === "User Defined List")
    {
        $("#showList").show();


        //addCompTmpl +='<select class="form-control" id="list_type">';
        addCompTmpl += '<option value="---select---">---select---</option>';
        for (var j = 0; j < masterDataList.userdefined.length; j++) {
            addCompTmpl += "<option value='" + masterDataList.userdefined[j].id + "'>" + masterDataList.userdefined[j].listName + "</option>";
        }

        //addCompTmpl +='</select>';
        $('#Predefined_list').html(addCompTmpl);
    }
}



function resetOnSelectOfType() {

    var type = $('#comp_type').find(":selected").text().trim();
    // var dbvalues=$('#default_value').find(":selected").text();

    if (type === "Location") { // added location by jyoti, Feature #30909
                $("#inlineCheckbox2").parent().attr('class', 'checked');
                $("#inlineCheckbox2").attr('checked', true);
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").hide();
        $("#addCompMandatory").hide(); // added location by jyoti, Feature #30909
        $("#addCompEditable").hide(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#enable_geo_tagging").hide();
        
    }
    else if (type === "Textbox" || type === "Textarea" || type === "Number") {
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").show();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#enable_geo_tagging").hide();
    }
    else if (type === "Dropdown" || type === "Radio" || type === "Checkboxes") {
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").show();
        $("#addCompPlaceHolder").hide();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_placeholder").val("");
        $("#enable_geo_tagging").hide();
    } else if (type === "Date") {
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").hide();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#comp_placeholder").val("");
        $("#enable_geo_tagging").hide();
    } else if (type === "Predefined Lists") {
        $("#List_Display").show();
        $("#addCompValueLabel").hide();
        $("#list_type").val("----select-----");
        
        $("#comp_placeholder").val("");
        $("#addCompPlaceHolder").hide()
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#enable_geo_tagging").hide();
    } else if (type === "Image") {
        if ($("#form_trigger_event").val() == "1" && $("#inlineCheckbox2").is(':checked') == false) {
            $("#enable_geo_tagging").hide();
            $("#showList").hide();
            $("#List_Display").hide();
            $("#Predefined_list").val("");
            $("#list_type").val("");
            $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
            $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        } else {
            $("#enable_geo_tagging").show();
            $("#showList").hide();
            $("#List_Display").hide();
            $("#Predefined_list").val("");
            $("#list_type").val("");
        }
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").hide();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#comp_placeholder").val("");
    } else {
//       $("#showList").hide();
//      $("#List_Display").hide();
//      $("#Predefined_list").val("");
//        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").show();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#enable_geo_tagging").hide();
    }


}
//#11446
//New function for editing menu
function resetOnSelectOfTypeForEdit() {

    var type = $('#comp_type').find(":selected").text().trim();
    // var dbvalues=$('#default_value').find(":selected").text();

    if (type === "Location") { // added location by jyoti, Feature #30909
                $("#inlineCheckbox2").parent().attr('class', 'checked');
                $("#inlineCheckbox2").attr('checked', true);
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").hide();
        $("#addCompMandatory").hide(); // added location by jyoti, Feature #30909
        $("#addCompEditable").hide(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#enable_geo_tagging").hide();
        
    }
    else if (type === "Textbox" || type === "Textarea" || type === "Number") {
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").show();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#enable_geo_tagging").hide();
    }
    else if (type === "Dropdown" || type === "Radio" || type === "Checkboxes") {
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").show();
        $("#addCompPlaceHolder").hide();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_placeholder").val("");
        $("#enable_geo_tagging").hide();
    } else if (type === "Date") {
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").hide();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#comp_placeholder").val("");
        $("#enable_geo_tagging").hide();
    } else if (type === "Predefined Lists") {
        $("#List_Display").show();
        $("#addCompValueLabel").hide();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        //   $("#list_type").val("----select-----");
        $("#comp_placeholder").val("");
        $("#addCompPlaceHolder").hide()
        $("#enable_geo_tagging").hide();
    } else if (type === "Image") {
        if ($("#form_trigger_event").val() == "1" && $("#inlineCheckbox2").is(':checked') == false) {
            $("#enable_geo_tagging").hide();
            $("#showList").hide();
            $("#List_Display").hide();
            $("#Predefined_list").val("");
            $("#list_type").val("");
            $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
            $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        } else {
            $("#enable_geo_tagging").show();
            $("#showList").hide();
            $("#List_Display").hide();
            $("#Predefined_list").val("");
            $("#list_type").val("");
            $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
            $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        }
        $("#showList").hide();
        $("#List_Display").hide();
        $("#Predefined_list").val("");
        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").hide();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#comp_placeholder").val("");
    } else {
//       $("#showList").hide();
//      $("#List_Display").hide();
//      $("#Predefined_list").val("");
//        $("#list_type").val("");
        $("#addCompValueLabel").hide();
        $("#addCompPlaceHolder").show();
        $("#addCompMandatory").show(); // added location by jyoti, Feature #30909
        $("#addCompEditable").show(); // added location by jyoti, Feature #30909
        $("#comp_values").val("");
        $("#enable_geo_tagging").hide();
    }
}
//
function checkPreview() {

    //var t = $('#sample_21i').DataTable();
    //  var size=t.fnGetData().length;
    // console.log(rows);
    var preview = "";
    var type = "";
    var options = [];

    var date = new Date();
    var m = date.getMonth() + 1;
    if (m < 10) {
        m = '0' + m;
    }
    var y = date.getFullYear();
    var d = date.getDate();
    if (d < 10) {
        d = '0' + d;
    }
    var firstDay = d + '/' + m + '/' + y;

    var editable = "";

    for (var i = 0; i < tableData.length; i++)
    {
        //type= $("#row_type_"+i).val();
        type = tableData[i].fieldType;
        editable = tableData[i].editField;


        preview += '<div class="form-group"><label class="col-md-4 control-label">' + tableData[i].labelName + '</label>';

        if (type === 'Textbox' || type === "Location") { // added location by jyoti, Feature #30909
            preview += '<div class="col-md-8"> <input type="text" class="form-control" placeholder="' + tableData[i].placeHolder + '" ' + editable + '></div> ';
        } else if (type === 'Dropdown') {
            preview += ' <div class="col-md-8"><select class="form-control" ' + editable + '>';
            options = tableData[i].option.split('\n');
            //options=$("#row_options_"+i).val().split('\n');
            for (var j = 0; j < options.length; j++) {
                preview += "<option value='" + options[j] + "'>" + options[j] + "</option>";
            }
            preview += "</select></div>"
        } else if (type === 'Number') {
            preview += '<div class="col-md-8"> <input type="text" class="form-control" placeholder="' + tableData[i].placeHolder + '" ' + editable + '></div> ';
        } else if (type === 'Date') {
            preview += '<div class="col-md-8">';
            preview += '<div class = "input-group date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
            preview += '<input type = "text" class = "form-control" name = "from" id = "id_startDate" value="' + firstDay + '" ' + editable + '>';
            preview += '</div>';
            preview += '<span class="help-block">' + tableData[i].placeHolder + '</span></div>';
        } else if (type === 'Checkboxes') {
            preview += ' <div class="col-md-8"><div class="checkbox-list"> ';
            options = tableData[i].option.split('\n');
            // options=$("#row_options_"+i).val().split('\n');
            for (var j = 0; j < options.length; j++) {
                preview += '<label class="checkbox-inline"><input type="checkbox">' + options[j] + ' </label>';
            }
            preview += "</div></div>";
        } else if (type === 'Radio') {
            preview += ' <div class="col-md-8"><div class="radio-list"> ';
            options = tableData[i].option.split('\n');
            //options=$("#row_options_"+i).val().split('\n');
            for (var j = 0; j < options.length; j++) {
                preview += '<label class="radio-inline"><input type="radio" name="radio_' + i + '" >' + options[j] + ' </label>';
            }
            preview += "</div></div>";
        } else if (type === 'Textarea') {
            preview += '<div class="col-md-8"> <textarea class="form-control" rows="3" placeholder="' + tableData[i].placeHolder + '" ' + editable + '></textarea></div> ';
        } else if (type == "Image") {
            preview += '<div class="col-md-8">';
            preview += '<p class="form-control-static"><img src="assets/img/noimage.jpg"/></p>';
            preview += '</div>';
        }
        else if (type == "Predefined Lists") {
            preview += ' <div class="col-md-8"><select class="form-control">';
            preview += '<option> </option>';
            preview += '</select></div>';
        }
        else if (type == "Time") {
            preview += '<div class="col-md-8">';
            // preview += '<input type="hidden"  id="fieldid" value="'+fieldid+'">';

            preview += '<input type="hidden" name="time"  value="" >';

            preview += '<select name="time1" type="time1">';

            preview += '<option value="HH">HH</option>';
            preview += '<option value="01">01</option>';
            preview += '<option value="02">02</option>';
            preview += '<option value="03">03</option>';
            ;
            preview += '<option value="04">04</option>';
            preview += '<option value="05">05</option>';
            preview += '<option value="06">06</option>';
            preview += '<option value="07">07</option>';
            preview += '<option value="08">08</option>';
            preview += '<option value="09">09</option>';
            preview += '<option value="10">10</option>';
            ;
            preview += '<option value="11">11</option>';
            preview += '<option value="12">12</option>';
            preview += '</select>';
            preview += ' <select name="time1"  type="time1">';

            preview += '<option value="MM">MM</option>';
            preview += '<option value="00">00</option>';
            preview += '<option value="01">01</option>';
            preview += '<option value="02">02</option>';
            preview += '<option value="03">03</option>';
            preview += '<option value="04">04</option>';
            preview += '<option value="05">05</option>';
            preview += '<option value="06">06</option>';
            preview += '<option value="07">07</option>';
            preview += '<option value="08">08</option>';
            preview += '<option value="09">09</option>';
            preview += '<option value="10">10</option>';
            preview += '<option value="11">11</option>';
            preview += '<option value="12">12</option>';
            preview += '<option value="13">13</option>';
            preview += '<option value="14">14</option>';
            preview += '<option value="15">15</option>';
            preview += '<option value="16">16</option>';
            preview += '<option value="17">17</option>';
            preview += '<option value="18">18</option>';
            preview += '<option value="19">19</option>';
            preview += '<option value="20">20</option>';
            preview += '<option value="21">21</option>';
            preview += '<option value="22">22</option>';
            preview += '<option  value="23">23</option>';
            preview += '<option value="24">24</option>';
            preview += '<option value="25">25</option>';
            preview += '<option value="26">26</option>';
            preview += '<option value="27">27</option>';
            preview += '<option value="28">28</option>';
            preview += '<option value="29">29</option>';
            preview += '<option value="30">30</option>';
            preview += '<option value="31">31</option>';
            preview += '<option value="32">32</option>';
            preview += '<option value="33">33</option>';
            preview += '<option value="34">34</option>';
            preview += '<option value="35">35</option>';
            preview += '<option value="36">36</option>';
            preview += '<option value="37">37</option>';
            preview += '<option value="38">38</option>';
            preview += '<option value="39">39</option>';
            preview += '<option value="40">40</option>';
            preview += '<option value="41">41</option>';
            preview += '<option value="42">42</option>';
            preview += '<option value="43">43</option>';
            preview += '<option value="44">44</option>';
            preview += '<option value="45">45</option>';
            preview += '<option value="46">46</option>';
            preview += '<option value="47">47</option>';
            preview += '<option value="48">48</option>';
            preview += '<option value="49">49</option>';
            preview += '<option value="50">50</option>';
            preview += '<option value="51">51</option>';
            preview += '<option value="52">52</option>';
            preview += '<option value="53">53</option>';
            preview += '<option value="54">54</option>';
            preview += '<option value="55">55</option>';
            preview += '<option value="56">56</option>';
            preview += '<option value="57">57</option>';
            preview += '<option value="58">58</option>';
            preview += '<option value="59">59</option>';

            preview += '</select>';
            preview += '<select name="time1" type="time1">';

            preview += '<option value="AM">AM</option>';
            preview += '<option value="PM">PM</option>';

            preview += '</select>';
            preview += '</div>';
            //  preview += '<input type="button" value="Hidden Value" onclick="showHiddenValue();" />'
//                                preview += '</form>'


        }

        preview += '"</div> '

    }

    //console.log(cells);
    $("#form_preview_body").html(preview);
    $('.modal-title').html('Preview Form');
    App.initUniform();
    App.fixContentHeight();
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });


}




/* code for up and down of rows */
$(document).on('click', '.moveup', function (e) {
    var index = $(this).closest("tr").index();
    //console.log("helloindex21:"+index);
    if ((index - 1) >= 0) {
        var datatable = $('#sample_21i').dataTable()
        var data = datatable.fnGetData();
        datatable.fnClearTable();
        data.splice((index - 1), 0, data.splice(index, 1)[0]);
        datatable.fnAddData(data);

        tableData.splice((index - 1), 0, tableData.splice(index, 1)[0]); //Reorder in TableData
    }
    jQuery('.tooltips').tooltip();
});
$(document).on('click', '.movedown', function (e) {
    var index = $(this).closest("tr").index();

    if ((index + 1) >= 0) {
        var datatable = $('#sample_21i').dataTable();
        var data = datatable.fnGetData();
        datatable.fnClearTable();
        data.splice((index + 1), 0, data.splice(index, 1)[0]);
        datatable.fnAddData(data);
        tableData.splice((index + 1), 0, tableData.splice(index, 1)[0]); //Reorder in TableData
    }
    jQuery('.tooltips').tooltip();
});
/*
 $(document).on('click', '.deleteRow', function(e){
 var target_row = $(this).closest("tr").get(0); // this line did the trick
 var aPos = oTable.fnGetPosition(target_row); 
 
 oTable.fnDeleteRow(aPos);
 });
 */
function deleteRow(elem) {
    // var isDel= confirm("Are you sure. you want to Delete Field ? Related Data will also be deleted.");
    // if(isDel===false) return false;
    bootbox.dialog({
        message: "Are you sure you want to Delete Field ? Related Data will also be deleted.",
        title: "Delete Field",
        buttons: {
            yes: {
                label: "Yes",
                className: "btn-default",
                callback: function () {
                    var index = elem.closest("tr").index();
                    var target_row = elem.closest("tr").get(0); // this line did the trick
                    var aPos = $('#sample_21i').dataTable().fnGetPosition(target_row);
                    $('#sample_21i').dataTable().fnDeleteRow(aPos);
                    if (index > -1) {
                        //alert(tableData[index].fieldId);
                        deletedFields.push(tableData[index].fieldId);
                        tableData.splice(index, 1);  //Delete in tableData
                        //delete
                    }
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
}
//Time field as added in editRow method
function editRow(elem) {
    var index = elem.closest("tr").index();
    var field = tableData[index];
    console.log(field.fieldType);
    ///////
    $('#form_responsive').html('');

    var addCompTmpl = '';
    addCompTmpl += '<div class="modal-dialog"> <div class="modal-content"> <div class="modal-header"> ';
    addCompTmpl += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
    addCompTmpl += '<h4 class="modal-title">Update Component</h4></div>';
    addCompTmpl += '<div class="modal-body"><div class="scroller" style="height:300px" data-always-visible="1" data-rail-visible1="1">';
    addCompTmpl += '<div class="row"> <div class="col-md-12"><form class="form-horizontal" role="form" id="add_component"><div class="form-body"> ';

    addCompTmpl += '<div class="form-group"> <label class="col-md-4 control-label">Label</label> <div class="col-md-8">';
    addCompTmpl += '<input type="text" id="comp_label" class="form-control" placeholder="Label" value="' + field.labelName + '">	</div> 	</div> ';
    addCompTmpl += '<div class="form-group"> <label class="col-md-4 control-label">Type</label> <div class="col-md-8"> ';
    addCompTmpl += '<select class="form-control" id="comp_type" onchange="resetOnSelectOfType()">';
    addCompTmpl += '<option>Textbox</option> <option>Textarea</option> <option>Number</option> <option>Date</option> ';
    addCompTmpl += '<option>Location</option>'; // added by jyoti, Feature #30909
    addCompTmpl += '<option>Dropdown</option> <option>Predefined Lists</option> <option>Checkboxes</option> <option>Radio</option><option>Image</option><option>Time</option> ';
    addCompTmpl += '</select></div></div>';
    //Editing task
    addCompTmpl += '<div class="form-group" id=List_Display style="display:none"> <label class="col-md-4 control-label">Value Should be fetch from</label> <div class="col-md-8"> ';
    addCompTmpl += '<select class="form-control" id="list_type" onchange="resetOnSelectOfList()">';
    addCompTmpl += '<option>----select-----</option><option value="1">System Defined List</option> <option value="2">User Defined List</option>';
    addCompTmpl += '</select></div></div>';

    addCompTmpl += '<div class="form-group" id="showList" style="display:none"> <label class="col-md-4 control-label">Lists names From Master data</label> <div class="col-md-8" ><select id="Predefined_list" class="form-control"></select> </div></div>';
//end task
    addCompTmpl += '<div class="form-group" id="enable_geo_tagging">';
    addCompTmpl += '<label class="col-md-4 control-label">Enable Geo-tagging</label>';
    addCompTmpl += '<div class="col-md-8">';
    addCompTmpl += '<div class="checkbox-list">';
    addCompTmpl += '<label class="checkbox-inline">';
    if (field.geoTagEnable == 1) {
        addCompTmpl += '<input type="checkbox" id="enable_geo_check" checked></label>';
    } else {
        addCompTmpl += '<input type="checkbox" id="enable_geo_check"></label>';
    }
    addCompTmpl += '</div>';
    addCompTmpl += '</div>';
    addCompTmpl += '</div>';
    addCompTmpl += '<div class="form-group" id="addCompPlaceHolder"> <label class="col-md-4 control-label">Placeholder</label><div class="col-md-8">';
    addCompTmpl += '<input type="text" id="comp_placeholder" class="form-control" placeholder="Placeholder" value="' + field.placeHolder + '"></div></div>';
    addCompTmpl += '<div class="form-group" id="addCompMandatory"><label class="col-md-4 control-label">Mandatory</label><div class="col-md-8"><div class="radio-list"> ';
    if (field.mandField == 1) {
        addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios1" id="optionsRadios27" value=1 checked> Yes </label>';
        addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios1" id="optionsRadios28" value=0 > No </label> ';
    } else {
        addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios1" id="optionsRadios27" value=1 > Yes </label>';
        addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios1" id="optionsRadios28" value=0 checked> No </label> ';
    }
    addCompTmpl += '</div></div></div>';
    addCompTmpl += '<div class="form-group" id="addCompEditable"><label class="col-md-4 control-label">Editable</label><div class="col-md-8"><div class="radio-list"> ';
    if (field.editField == 1) {
        addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios" id="optionsRadios25" value=1 checked> Yes </label>';
        addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios" id="optionsRadios26" value=0 > No </label> ';
    } else {
        addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios" id="optionsRadios25" value=1 > Yes </label>';
        addCompTmpl += '<label class="radio-inline">	<input type="radio" name="optionsRadios" id="optionsRadios26" value=0 checked> No </label> ';
    }
    addCompTmpl += '</div></div></div>';
    addCompTmpl += '<div class="form-group" id="addCompValueLabel"> <label class="col-md-4 control-label">Values</label> <div class="col-md-8"> ';
    addCompTmpl += '<textarea class="form-control" id="comp_values" rows="3" placeholder="Enter Values separated by new line"></textarea>';
    addCompTmpl += '</div> </div> ';
    addCompTmpl += '</div></form></div></div></div></div>';
    addCompTmpl += '<div class="modal-footer" id ="add_comp_footer"> ';
    addCompTmpl += '<button type="button" class="btn btn-info" onclick="addFieldRow(\'update\',' + index + ');">Update</button>';
    addCompTmpl += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    addCompTmpl += '</div> </div> ';

    $('#form_responsive').html(addCompTmpl);
    $("#list_type").val(field.fieldType);

    if (field.fieldType === "Predefined Lists")
    {
        var flag = true;
        for (var j = 0; j < masterDataList.userdefined.length; j++)
        {
            if (!masterDataList.userdefined[j].id !== null && masterDataList.userdefined[j].id == field.listIdFK && field.listIdFK > 1)
            {
                flag = true;
                break;
            } else {
                flag = false;
            }

        }

        if (flag == false)
        {
            $("#list_type").val("2");
            // $("#Predefined_list").show();
            $('#addCompValueLabel').hide();
            $('#comp_type').val(field.fieldType);
            //   $("#comp_type").prop("disabled", true);
            $('#addCompPlaceHolder').hide();

            $("#List_Display").show();
            resetOnSelectOfList();
            $("#Predefined_list").val("---select---");
        }

        for (var j = 0; j < masterDataList.userdefined.length; j++)
        {
            if (!masterDataList.userdefined[j].id !== null && masterDataList.userdefined[j].id == field.listIdFK)
            {


                $("#list_type").val("2");
                // $("#Predefined_list").show();
                $('#addCompValueLabel').hide();
                $('#comp_type').val(field.fieldType);
                //   $("#comp_type").prop("disabled", true);
                $('#addCompPlaceHolder').hide();

                $("#List_Display").show();
                resetOnSelectOfList();
                $("#Predefined_list").val(field.listIdFK);


            }
        }
        for (var j = 0; j < masterDataList.systemDefined.length; j++)
        {
            if (masterDataList.systemDefined[j].id !== null && masterDataList.systemDefined[j].id == field.listIdFK)
            {

                $("#list_type").val("1");
                $('#addCompValueLabel').hide();
                $('#comp_type').val(field.fieldType);

                $('#addCompPlaceHolder').hide();
                $("#List_Display").show();
                resetOnSelectOfList();
                $("#Predefined_list").val(field.listIdFK);
            }

            resetOnSelectOfTypeForEdit();
        }



    } else {

        $('#comp_type').val(field.fieldType);
        $("#comp_values").val(field.option);
    }
    resetOnSelectOfTypeForEdit(); //set default values on editing row call after setting $('#comp_type').val() .. since that method checks on basis of type

    App.fixContentHeight();
    App.initUniform();
    App.callHandleScrollers(); // new method added


}
/* End of for up and down code */


function submitForm(action, formId) {

    if (validateForm() === false) {
        $('#pleaseWaitDialog').modal('hide');
        return false;
    }
    var submitFormTemplate = "";
    var addformName = document.getElementById("addformName").value;
    var isActive = 0;

    if ($('#inlineCheckbox1').parent().attr('class') == 'checked')
        isActive = 1;
    var relatedTo = document.getElementById("relatedTo").value;
    var filledBy = document.getElementById("form_trigger_event").value;
    if (filledBy.trim() == "") {
        fieldSenseTosterError("Please select an option for When to be filled by User", true);
        return false;
    }
    var SubmitCaption = document.getElementById("SubmitCaption").value;
    var mobileOnly = 0;

    if ($('#inlineCheckbox2').parent().attr('class') == 'checked')
        mobileOnly = 1;

    //var geoTagEnable=false;
    //set order of fields before sending  
    for (i = 0; i < tableData.length; i++) {
        tableData[i].fieldOrder = (i + 1);
    }
    //end

    //if(geoTagEnable==true && filledBy=="1" && mobileOnly==0){
    //	fieldSenseTosterError("You cant", true);
    //	return false;
    //}

    if (action === 'add') {
        formId = 0;
    }

    var formData = {
        id: formId,
        formName: addformName,
        active: isActive,
        form_related_to: relatedTo,
        form_trigerred_event: filledBy,
        submit_caption: SubmitCaption,
        mobileOnly: mobileOnly,
        tableData: tableData,
        deletedFields: deletedFields,
        createdBy: loginUserId,
        updatedBy: loginUserId
    }

    var httpType = 'POST';
    var formUrl = "/adminCustomForms";
    if (action === 'update') {
        httpType = 'PUT';
        formUrl = "/adminCustomForms/" + formId;
    }

    var jsonData = JSON.stringify(formData);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: httpType,
        url: fieldSenseURL + formUrl, //need to change this url
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        data: jsonData,
        success: function (data) {
            if (data.errorCode == "0000") {
                //loadLeftNav();
                var formid = data.result.id; // get this from return data
                if (action === 'add') {
                    clevertap.event.push("Create Form", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                        "Source": "Web",
                        "Account name": accountNamecookie,
                        "UserRolecookie": UserRolecookie,
                    });
                    var templ = '<li id="leftnav_form_' + formid + '" class="leftnav_form"><a class="showSingle" onclick="editFormTemplate(' + formid + ')"> <span class="barrow"></span>'; //pass formid				
                    templ += '<span class="membername"  title="' + data.result.formName + '">' + data.result.formName + '</span></a></li>';
                    $("#formListMenu").prepend(templ);
                    setBlankForm();
                    //$("#leftnav_form_"+formid).addClass("active");
                } else {
                    var templ = '<a onclick="editFormTemplate(' + formid + ')" class="showSingle">';
                    templ += '<span class="barrow"></span><span class="membername" title="' + data.result.formName + '">' + data.result.formName + '</span></a>';
                    $("#leftnav_form_" + formid).html(templ);
                    $("#leftnav_form_" + formid + ">a").click();
                }
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
            }
        }
    });
    // $("#add_form_footer").find(".btn").removeAttr("disabled");;

}


function deleteForm(id) {
    //var isDel= confirm("Are you sure you want to Delete the Form ? Related Data will also be deleted.");
    //if(isDel===false) return false;
    bootbox.dialog({
        message: "Are you sure you want to Delete the Form ? Related Data will also be deleted.",
        title: "Delete Form",
        buttons: {
            yes: {
                label: "Yes",
                className: "btn-default",
                callback: function () {
                    $('#pleaseWaitDialog').modal('show');
                    $.ajax({
                        type: "DELETE",
                        url: fieldSenseURL + "/adminCustomForms/" + id,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        asyn: true,
                        success: function (data) {
                            if (data.errorCode == '0000') {
                                setBlankForm("delete");
                                $("#leftnav_form_" + id).remove();
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                $('#pleaseWaitDialog').modal('hide');
                            } else {
                                fieldSenseTosterError(data.errorMessage, true);
                                $('#pleaseWaitDialog').modal('hide');
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

}

function setBlankForm(action) {
    $(".leftnav_form").removeClass("active");
    // $("#addformName").val(''); // form name
    //$("#relatedTo").val(1);
    //$("#form_trigger_event").val(1);
    // $("#SubmitCaption").val('');
    tableData = [];
    $('#sample_21i').dataTable().fnClearTable();
    document.add_form.reset();
    App.updateUniform();
    // $("#inlineCheckbox1").parent().removeClass('checked');
    //$("#inlineCheckbox2").parent().removeClass('checked');
    if (action == "create" || action == "delete") {

        //$("#add_form_footer button:eq(0)" ).html("Save");
        //$("#add_form_footer button:eq(0)" ).attr("onclick","submitForm('add')");
        //$("#add_form_footer button:eq(1)" ).html("Reset");
        //$("#add_form_footer button:eq(1)" ).attr("onclick","document.add_form.reset();App.updateUniform();

        var foootertmpl = '<button type="button" class="btn btn-info" style="margin-right:3px" onclick="submitForm(\'add\');">Save</button>';
        foootertmpl += '<button type="button" class="btn btn-default" style="margin-right:3px" onClick="document.add_form.reset();App.updateUniform();ClearTimefield();$(\'#sample_21i\').DataTable().fnClearTable();">Reset</button>';
        foootertmpl += '<button type="button" class="btn btn-default" data-toggle="modal" href="#form_preview" onclick="checkPreview();">Preview</button>';
        $("#add_form_footer").html(foootertmpl);
    }
    //App.initUniform();
    //App.fixContentHeight();
}
function getMasterData() {

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/predefinedList/getAllListNames",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        asyn: true,
        success: function (data) {
            masterDataList = data.result;
        }
    });
}

function clearAddComponentDet() {
    $("#comp_label").val('');
    //$('#comp_type').find(":selected").text('Textbox'); //causing bug also not necessary hence commenting
    $("#comp_placeholder").val('');

    $("#comp_values").val('');
    var htmlFooter = '<button type="button" class="btn btn-info" onclick="addFieldRow(\'add\');">Add</button>';
    htmlFooter += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
    $('#add_comp_footer').html(htmlFooter);
}

function validateField() {
    var listType = $('#list_type').find(":selected").text().trim();
    var listName = $('#Predefined_list').find(":selected").text().trim();
    var label = $("#comp_label").val().trim();
    var type = $('#comp_type').find(":selected").text().trim();
    var options = $("#comp_values").val().trim();
    if (label === "") {
        fieldSenseTosterError("Enter Label Name", true);
        return false;
    }
    if (type === "Predefined Lists") {
        if (listType === "" || listType === "----select-----")
        {
            fieldSenseTosterError("Select a value from the dropdown", true);
            return false;
        }
        if (listName === "" || listName === "---select---")
        {
            fieldSenseTosterError("Select a value from the dropdown", true);
            return false;
        }
        return true;

    }
    if (type === "Checkboxes" || type === "Radio" || type === "Dropdown") {


        return true;
        if (options === "") {
            fieldSenseTosterError("Enter option values for type " + type, true);
            return false;
        }
    }
    return true;
}

function validateForm() {
    var addformName = document.getElementById("addformName").value.trim();
    if (addformName === "") {
        fieldSenseTosterError("Enter Form Name", true);

        // toastr["info"]('<div><input class="input-small" value="textbox"/>&nbsp;</div><div><button type="button" id="okBtn" class="btn btn-primary">Close me</button><button type="button" id="surpriseBtn" class="btn" style="margin: 0 8px 0 8px">Surprise me</button></div>');
        return false;
    }
    var submitCaption = document.getElementById("SubmitCaption").value;
    if (submitCaption === "") {
        fieldSenseTosterError("Enter Submit button caption", true);
        return false;
    }

    if (tableData.length < 1) {
        fieldSenseTosterError("Enter Atleast one field", true);
        return false;
    }
}

$('#sample_21i').dataTable({
    "bFilter": false,
    "bLengthChange": false,
    "bPaginate": false,
    "bSort": false,
    "fnDrawCallback": function (oSettings) {
        /* Need to redo the counters if filtered or sorted */
        //if ( oSettings.bSorted || oSettings.bFiltered )
        //{
        for (var i = 0, iLen = oSettings.aiDisplay.length; i < iLen; i++)
        {
            $('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr).html(i + 1);
        }
        //}
    }
    //"aoColumnDefs": [{ "bVisible": false, "aTargets": [6] }]
});


function ClearTimefield()
{
    alert("clear");
}
   