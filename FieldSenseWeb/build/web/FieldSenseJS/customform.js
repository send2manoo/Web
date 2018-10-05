var intervalAdminCustomer = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {
            loggedinUserImageData();
            loggedinUserData();
            custFormNamesFill();
            // getCustomerNames();
            //     getMasterData("41");
            window.clearTimeout(intervalAdminCustomer);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
var customerNameList = [];
var masterDataList = [];
var userDefinedList = [];
var systemDefinedList = [];
//added by nikhil
var loginUserIdcookie = fieldSenseGetCookie("loginUserIdcookie");
var loginUserNamecookie = fieldSenseGetCookie("loginUserNamecookie");
var accountIdcookie = fieldSenseGetCookie("accountIdcookie");
var accountNamecookie = fieldSenseGetCookie("accountNamecookie");
var UserRolecookie = fieldSenseGetCookie("UserRolecookie");
console.log("accountNamecookie " + accountNamecookie);
//ended by nikhil
//Method for getting customer list 
function getCustomerNames()
{
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/customer/allNames", // change url
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            for (var i = 0; i < data.result.length; i++)
            {
                customerNameList.push(data.result[i]);
            }
            //  return customerNameList;
        }
    });
}
function getMasterData(id) {


}
function  custFormNamesFill() {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/adminCustomForms/web/active", // change url
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: false,
        success: function (data) {
            if (data.errorCode == '0000') {
                var formList = data.result;
                if (formList == "") {
                    $("#form_names").html("Please create atleast one form");
                } else {
                    var templ = "";
                    for (i = 0; i < formList.length; i++)
                    {
                        templ += '<li id="leftnav_form_' + formList[i].id + '" class="leftnav_form"><a class="showSingle"  onclick="fillcustform(' + formList[i].id + ')"> <span class="barrow"></span>';
                        templ += '<span class="membername" title="' + formList[i].formName + '">' + formList[i].formName + '</span></a></li>';
                    }
                    $("#custFrmNameReports").html(templ);
                }
            }
        }
    });
}


function fillcustform(id) {

//getMasterData(id);
//if(flag==1)
//{

//Editing for large data.

    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/predefinedList/getAllListData/" + id,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        asyn: false,
        success: function (data) {

            masterDataList = data.result;
            userDefinedList = masterDataList.UserDefinedList;
            systemDefinedList = masterDataList.SystemDefinedList;
            //return 1;


//
            $(".leftnav_form").removeClass("active");
            $("#leftnav_form_" + id).addClass("active");
//actionURL = fieldSenseURL + "/CustomFormsReport/image/save/"+userToken;
            var submitcaption = "Save";
            var preview = '<div class="portlet"><div class="portlet-title"><div class="caption"></div></div><div class="portlet-body form"><form class="form-horizontal"  role="form"  method="POST" id="submit_form" name="submit_form"><div class="form-body">';
            var type = "";
            var options = [];
            var caption;
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
            var tableData = [];
            var editable = "";
            var fieldid;
            var editField;
            var mandField;
            var listId;
            var listName;
            var listType;
           // var InputName;
            //var count=0;   
            $('#pleaseWaitDialog').modal('show');
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
                asyn: false,
                success: function (data) {
                    tableData = data.result.tableData;
                    caption = data.result.formName;
                    
                    for (var i = 0; i < tableData.length; i++)
                    {
                        //type= $("#row_type_"+i).val();
                        type = tableData[i].fieldType;
                        editable = tableData[i].editField;
                        fieldid = tableData[i].fieldId;
                        submitcaption = data.result.submit_caption;
                        editField = tableData[i].editField;
                        mandField = tableData[i].mandField;
                        listId = tableData[i].listIdFK;
                        listName = tableData[i].listName;
                        listType = tableData[i].listType;
//                        var InputTime ;
//                        if(type =="Time")
//                        {
//                            InputTime = 'nikhil';
//                        }
                           
                        // databaseValues=tableData[i].dataBaseValues;
                        if (mandField == 1) {
                            preview += '<div class="form-group"><label class="col-md-4 control-label">' + tableData[i].labelName + '<span style="color:red"> *</label>';
                        } else {
                            preview += '<div class="form-group"><label class="col-md-4 control-label">' + tableData[i].labelName + '</label>';
                        }
                        if (type == 'Textbox' || type == "Location") { // added location by jyoti, Feature #30909

                            if (editField == 1) {
                                preview += '<div class="col-md-8"> <input type="text" name="Textbox" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + '></div> ';
                            } else {
                                preview += '<div class="col-md-8"> <input type="text" name="Textbox" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + '  readonly></div> ';
                            }
                        } else if (type == 'Number') {
                            if (editField == 1) {
                                preview += '<div class="col-md-8"> <input type="text" name="Number" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + '></div> ';
                            } else {
                                preview += '<div class="col-md-8"> <input type="text" name="Number" id="' + fieldid + '" class="form-control mand' + mandField + '" placeholder="' + tableData[i].placeHolder + '" ' + editable + '  readonly></div> ';
                            }
                        } else if (type == 'Dropdown') {
                            // if(databaseValues!=="Customer Name" && databaseValues!=="Address"){
                            if (editField == 1) {
                                preview += ' <div class="col-md-8"><select id="' + fieldid + '" name="Dropdown" class="form-control mand' + mandField + '" ' + editable + '>';
                            } else {
                                preview += ' <div class="col-md-8"><select name="Dropdown" id="' + fieldid + '" class="form-control mand' + mandField + '" ' + editable + '  disabled>';
                            }
                            preview += '<option value="---select---">---select---</option>';
                            options = tableData[i].option.split('\n');
                            //options=$("#row_options_"+i).val().split('\n');
                            for (var j = 0; j < options.length; j++) {
                                preview += "<option value='" + options[j] + "'>" + options[j] + "</option>";
                            }
                            preview += "</select></div>";
//                                  }else{
//                                       preview +=' <div class="col-md-8"><select id="'+fieldid+'" name="Dropdown" class="form-control mand'+mandField+'" '+editable+'>';
//					
//					preview +='<option value="---select---">---select---</option>';
//				      //	options=tableData[i].option.split('\n');
//				      	//options=$("#row_options_"+i).val().split('\n');
//				      	for(var j=0;j<customerNameList.length;j++){
//						preview +="<option value='"+customerNameList[j]+"'>"+customerNameList[j]+"</option>";
//				      	}
//				      preview +="</select></div>";
//                                  }


                        } else if (type == 'Date') {
                           
                            preview += '<div class="col-md-8">';
                            if (editField == 1) {
                                preview += '<div class = "input-group date-picker input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
                                preview += '<input type = "text" class = "form-control mand' + mandField + '" name = "Date" id="' + fieldid + '" value="' + firstDay + '" ' + editable + '>';
                            } else {
                                preview += '<div class = "input-group  input-daterange" data - date = "20/11/2012" data-date-format="dd/mm/yyyy">';
                                preview += '<input type = "text" class = "form-control mand' + mandField + '" name = "Date" id="' + fieldid + '" value="' + firstDay + '" ' + editable + '  readonly>';
                            }
                            preview += '</div>';
                            preview += '<span class="help-block">' + tableData[i].placeHolder + '</span></div>';
                        }

                        // nikhil


                        else if (type === "Time") {
                          
        // added by nikhil for Time field :- 28 aug 2017
                            if (editField == 1) {
                               
                        preview += '<div class="col-md-8">';
                           // preview += '<input type="hidden"  id="fieldid" value="'+fieldid+'">';
                         
                           preview += '<input type="hidden" name="time" class="time_field_class' + fieldid + '" id="'+fieldid+'"  value="" >';
                           
                                preview += '<select name="time1" onchange="setHour('+fieldid+');" id="'+fieldid+'" class="HH'+fieldid+'" type="time1">';

                                preview += '<option value="HH">HH</option>';
                                preview += '<option value="01">01</option>';
                                preview += '<option value="02">02</option>';
                                preview += '<option value="03">03</option>';;
                                preview += '<option value="04">04</option>';
                                preview += '<option value="05">05</option>';
                                preview += '<option value="06">06</option>';
                                preview += '<option value="07">07</option>';
                                preview += '<option value="08">08</option>';
                                preview += '<option value="09">09</option>';
                                preview += '<option value="10">10</option>';;
                                preview += '<option value="11">11</option>';
                                preview += '<option value="12">12</option>';
                                preview += '</select>';
                                preview += ' <select name="time1" onchange="setMin('+fieldid+');" id="'+fieldid+'" class="MM'+fieldid+'" type="time1">';

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
                                preview += '<select name="time1" onchange="setAmPm('+fieldid+');" id="'+fieldid+'" class="am'+fieldid+'" type="time1">';

                                preview += '<option value="AM">AM</option>';
                                preview += '<option value="PM">PM</option>';

                                preview += '</select>';
                                preview += '</div>';
                                //  preview += '<input type="button" value="Hidden Value" onclick="showHiddenValue();" />'
//                                preview += '</form>'
                                
                            }
                            //InputName = 'Time';
                        }
                        // ended by nikhil 
                        else if (type == 'Checkboxes') {
                            preview += ' <div class="col-md-8"><div class="checkbox-list check-mand' + mandField + '"> ';
                            options = tableData[i].option.split('\n');
                            if (editField == 1) {
                                // options=$("#row_options_"+i).val().split('\n');
                                for (var j = 0; j < options.length; j++) {
                                    preview += '<label class="checkbox-inline"><input type="checkbox" name = "Checkboxes" class="mand' + mandField + '" id="' + fieldid + '">' + options[j] + ' </label>';
                                }
                            } else {
                                for (var j = 0; j < options.length; j++) {
                                    preview += '<label class="checkbox-inline"><input type="checkbox" disabled  name = "Checkboxes" class="mand' + mandField + '" id="' + fieldid + '">' + options[j] + ' </label>';
                                }
                            }
                            preview += "</div></div>";
                        } else if (type == 'Radio') {
                            preview += ' <div class="col-md-8"><div class="radio-list radio-mand' + mandField + '"> ';
                            options = tableData[i].option.split('\n');
                            //options=$("#row_options_"+i).val().split('\n');
                            if (editField == 1) {
                                for (var j = 0; j < options.length; j++) {
                                    preview += '<label class="radio-inline"><input type="radio"  id="' + fieldid + '"  name = "Radio" class="mand' + mandField + '" name="radio_' + i + '" >' + options[j] + ' </label>';
                                }
                            } else {
                                for (var j = 0; j < options.length; j++) {
                                    preview += '<label class="radio-inline"><input type="radio"  id="' + fieldid + '" name = "Radio" class="mand' + mandField + '"  disabled name="radio_' + i + '" >' + options[j] + ' </label>';
                                }
                            }
                            preview += "</div></div>";
                        } else if (type == 'Image') {
                            if (editField == 1) {
                                preview += '<div class="col-md-8">';
                                preview += '<div class="fileupload fileupload-new" data-provides="fileupload">';
                                preview += '<div class="fileupload-new thumbnail" style="width: 150px; height: 150px;">';
                                preview += '<img src="assets/img/noimage.jpg"/>';
                                preview += '</div>';
                                preview += '<div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 150px; max-height: 150px; line-height: 20px;">';
                                preview += '</div>';
                                preview += '<div>';
                                preview += '<span class="btn btn-default btn-file">';
                                preview += '<span class="fileupload-new">';
                                preview += '<i class="fa fa-paperclip"></i> Select image';
                                preview += '</span>';
                                preview += '<span class="fileupload-exists">';
                                preview += '<i class="fa fa-undo"></i> Change';
                                preview += '</span>';
                                preview += '<input type="file" name = "Image" class="default  mand' + mandField + '" id="' + fieldid + '" name="fileUpload"/>';
                                preview += '</span>';
                                preview += '<a href="#" class="btn btn-danger fileupload-exists" data-dismiss="fileupload"><i class="fa fa-trash-o"></i> Remove</a>';
                                preview += '</div>';
                                preview += '</div>';
                                preview += '</div>';
                            } else {
                                preview += '<div class="col-md-8">';
                                preview += '<div class="fileupload fileupload-new" data-provides="fileupload[]">';
                                preview += '<div class="fileupload-new thumbnail" style="width: 150px; height: 150px;">';
                                preview += '<img src="assets/img/noimage.jpg"/>';
                                preview += '</div>';
                                preview += '<div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 150px; max-height: 150px; line-height: 20px;">';
                                preview += '</div>';
                                preview += '<div style="display:none">';
                                preview += '<span class="btn btn-default btn-file">';
                                preview += '<span class="fileupload-new">';
                                preview += '<i class="fa fa-paperclip"></i> Select image';
                                preview += '</span>';
                                preview += '<span class="fileupload-exists">';
                                preview += '<i class="fa fa-undo"></i> Change';
                                preview += '</span>';
                                preview += '<input type="file" name = "Image" class="default  mand' + mandField + '" id="' + fieldid + '" name="fileUpload[]"/>';
                                preview += '</span>';
                                preview += '<a href="#" class="btn btn-danger fileupload-exists" data-dismiss="fileupload"><i class="fa fa-trash-o"></i> Remove</a>';
                                preview += '</div>';
                                preview += '</div>';
                                preview += '</div>'
                            }
                        } else if (type == 'Textarea') {
                            if (editField == 1) {
                                preview += '<div class="col-md-8"> <textarea class="form-control mand' + mandField + '" id="' + fieldid + '" name = "Textarea" rows="3" placeholder="' + tableData[i].placeHolder + '" ' + editable + '></textarea></div> ';
                            } else {
                                preview += '<div class="col-md-8 "> <textarea class="form-control mand' + mandField + '" id="' + fieldid + '" name = "Textarea" rows="3" placeholder="' + tableData[i].placeHolder + '" ' + editable + '  readonly></textarea></div> ';
                            }
                        } else if (type === 'Predefined Lists')
                        {
                            //#11446
                            //Submitting form with predefined data.
                            var uOptionList;
                            var count = 0;
                            if (listType == "UserDefinedList")
                            {
                                for (var m = 0; m < userDefinedList.length; m++)
                                {
                                    //uOptionList=userDefinedList[m];
                                    if (userDefinedList[m].listName == listName && count == 0)
                                    {
                                        count++;
                                        uOptionList = userDefinedList[m].optionList;
                                        if (editField == 1) {
                                            preview += ' <div class="col-md-8"><select id="' + fieldid + '" name="Dropdown" class="form-control mand' + mandField + '" ' + editable + '>';
                                        } else {
                                            preview += ' <div class="col-md-8"><select name="Dropdown" id="' + fieldid + '" class="form-control mand' + mandField + '" ' + editable + '  disabled>';
                                        }
                                        preview += '<option value="---select---">---select---</option>';
                                        //   preview +=' <div class="col-md-8"><select id="'+fieldid+'" name="Dropdown" class="form-control mand'+mandField+'" '+editable+'>';
                                        for (var k = 0; k < uOptionList.length; k++) {
                                            preview += "<option value='" + uOptionList[k] + "'>" + uOptionList[k] + "</option>";
                                        }
                                        preview += "</select></div>";
                                    }

                                }
                            }
                            else//(listType=="SystemDefinedList")
                            {
                                count = 0;
                                for (var m = 0; m < systemDefinedList.length; m++)
                                {
                                    //uOptionList=userDefinedList[m];
                                    if (systemDefinedList[m].listName == listName && count == 0)
                                    {
                                        count++;
                                        uOptionList = systemDefinedList[m].optionList;
                                        if (editField == 1) {
                                            preview += ' <div class="col-md-8"><select id="' + fieldid + '" name="Dropdown" class="form-control mand' + mandField + '" ' + editable + '>';
                                        } else {
                                            preview += ' <div class="col-md-8"><select name="Dropdown" id="' + fieldid + '" class="form-control mand' + mandField + '" ' + editable + '  disabled>';
                                        }
                                        preview += '<option value="---select---">---select---</option>';
                                        // preview +=' <div class="col-md-8"><select id="'+fieldid+'" name="Dropdown" class="form-control mand'+mandField+'" '+editable+'>';
                                        for (var k = 0; k < uOptionList.length; k++) {
                                            preview += "<option value='" + uOptionList[k] + "'>" + uOptionList[k] + "</option>";
                                        }
                                        preview += "</select></div>";
                                    }
                                }
//                                   
//                                        //uOptionList=userDefinedList[m];
//                                        
//                                             preview +=' <div class="col-md-8"><select id="'+fieldid+'" name="Dropdown" class="form-control mand'+mandField+'" '+editable+'>';
//                                              for(var m=0;m<userDefinedList.length;m++)
//                                    {
//						preview +="<option value='"+userDefinedList[m].customerName+"'>"+userDefinedList[m].customerName+"</option>";
//                                             
//				      
//                                      
//                                    }
//                                    preview +="</select></div>";
                            }
                        }

                        preview += '</div> ';
                    }
                    preview += '</div></form><div class="form-actions nobg" style="padding: 20px 0px !important;" id="add_form_footer"><button type="button" class="btn btn-info" style="margin-right:3px;margin-left:10px;" onclick="saveformdetails(' + id + ','+fieldid+');">' + submitcaption + '</button><button type="button" class="btn btn-default" onClick="document.submit_form.reset();App.updateUniform();ClearTimefield()">Reset</button><div>';
                    $("#fill_form").html(preview);
                    $(".caption").html(caption);
                    $(".caption").attr('title', caption);
                    $(".caption").css({'max-width': '300px', 'overflow': 'hidden', 'text-overflow': 'ellipsis'});
                    App.initUniform();
                    App.fixContentHeight();
                    $('#pleaseWaitDialog').modal('hide');
                    $('.date-picker').datepicker({
                        rtl: App.isRTL(),
                        autoclose: true
                    });
                },
                error: function (xhr, ajaxOptions, thrownError)
                {
                    alert("in error:" + thrownError);
                }

            });
            //console.log(cells);
        }
    });
    // }
}
function ClearTimefield()

{
   
    $("input[type=hidden]").val('');
//    for(var i =0 ; i<=fieldIDArray.length ;i++ )
//    {    
//    $('.time_field_class' + fieldIDArray[i]).val("");
//    }
}

function saveformdetails(formid,fieldid) {
    //console.log("InputTime:"+InputTime);
    var filleddata = [];
    var arrayId =[];
    var timedata =[];
    var customFormImage;
    var previd;
    var field_value;
    var funexit;
    var radiodefault;
   // var fieldid;
    var fieldType;
    var count = 0;
    var indexListForImages = [];
    $($('#submit_form').prop('elements')).each(function () {
        if (this.type == 'checkbox') {

            /*if($(this).hasClass('mand1') && ($(this).parent().hasClass('checked')==false)){
             
             fieldSenseTosterError($(this).parent().parent().parent().text()+ " should be selected");
             funexit=false;
             return false;
             
             }else{	*/
          
            if ($(this).parent().attr('class') == 'checked') {
                count = count + 1;
                if (previd == this.id) {
                    field_value = field_value + "\n" + $(this).parent().parent().parent().text();
                    filleddata = filleddata.slice(0, filleddata.length - 1);
                    filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                } else {
                    field_value = $(this).parent().parent().parent().text();
                    filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                    previd = this.id;
                }
            }
            //}
        } else if (this.type == 'radio') {
            //if($(this).hasClass('mand1') && ($(this).parent().hasClass('checked')==false)){
            //	fieldSenseTosterError($(this).parent().parent().parent().text()+ " should be selected");
            //	funexit=false;
            //	return false;
            //}else{
            if ($(this).parent().attr('class') == 'checked') {
                count = count + 1;
                field_value = $(this).parent().parent().parent().text();
                filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
            }
            //}
        } else if (this.type == 'select-one') {
            console.log("Inside not hidden select");
            if ($(this).hasClass('mand1') && (this.value.trim() == "---select---")) {
                var lablename = $(this).parent().parent().find('label').text();
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be selected");
                funexit = false;
                return false;
            } else {
                field_value = this.value;
                console.log("this.value "+this.type);
                if (field_value == "---select---") {
                    filleddata.push({"field_id_fk": this.id, "field_value": "", "fieldType": this.name});
                } else if (!$(this).hasClass('HH'+this.id) &&!$(this).hasClass('MM'+this.id)&&!$(this).hasClass('am'+this.id)) {
                    filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                    count = count + 1;
                }
            }

        } else if (this.type == 'file') {
            console.log("Inside not file hidden");
            if ($(this).hasClass('mand1') && (this.value.trim() == "")) {
                var lablename = $(this).parent().parent().parent().parent().parent().find('label').text();
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be uploaded");
                funexit = false;
                return false;
            } else {
                if (this.value.trim() == "") {
                    filleddata.push({"field_id_fk": this.id, "field_value": "", "fieldType": "Image"});
                } else {
                    customFormImage = {"fieldId": this.id, "imageURL": ""};
                    indexListForImages.push(filleddata.length);
                    filleddata.push({"field_id_fk": this.id, "field_value": "", "fieldType": "Image", "customFormImage": customFormImage});
                }
                count = count + 1;
            }

        } else 
            if (this.type != "hidden") {
                console.log("Inside not hidden");
                if ($(this).hasClass('mand1') && (this.value.trim() == "")) {
                    var lablename = $(this).parent().parent().find('label').text();
                    fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should not be empty");
                    funexit = false;
                    return false;
                } else {
                    field_value = this.value;
                    //alert("field_value^^Text "+field_value);
                     console.log("this.name >> "+this.name);
                    filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                                    if (field_value.trim() != "")
                        count = count + 1;
                }
            }else
            // added by nikhil for time field :- 28 aug 2017
            if (this.type == "hidden" && $(this).hasClass('time_field_class' + this.id)) 
            {
                console.log("this.id "+this.id);
                if ($(this).hasClass('time_field_class' + this.id) && (this.value.trim() == "")) {
                    var lablename = $(this).parent().parent().find('label').text();
                    console.log("lablename "+lablename);
                    fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be selected");
                    funexit = false;
                  //  alert("this.id^^ "+this.id);
                    return false;
                }
                 else {
                    field_value = this.value;
                   // alert("field_value^^ "+field_value +" this.id"+this.id +" this.name "+this.name);
                    console.log("this.value in hidden "+this.type);
                    if(this.name !== 'expM' || this.name !=='expY' ||this.name !=='expAmPm')
                    {    
                    filleddata.push({"field_id_fk": this.id, "field_value": field_value.trim(), "fieldType": this.name});
                    if (field_value.trim() != "")
                        count = count + 1;                  
                    }
                    arrayId.push(this.id);
                   // $('.time_field_class' +this.id).val("");
                    //document.getElementsByClassName('time_field_class' + this.id).value="";
                }
                 }
              // ended by nikhil           
    });   
    /*var frm1 = document.forms['submit_form'];
     var numberElements = frm1.elements.length;
     //alert("numberElements:"+numberElements);
     for(var i = 0; i < numberElements; i++)
     {
     // alert(frm1.elements[i].getAttribute("type")); 
     
     } */
   
    //for checkbox validaton if it is mandatory
    if (funexit != false) {
        var checkmand;
        $(".check-mand1").each(function () {
            $(".check-mand1").find('.checkbox-inline').each(function () {
                if ($(this).find('span').hasClass('checked')) {
                    checkmand = true;
                    return false;
                } else {
                    checkmand = false;
                }
            });
            if (checkmand == false) {
                var lablename = $(this).parent().parent().find('.control-label').text();
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be selected");
                funexit = false;
                return false;
            }
        });
    }
    //for checkbox validaton  if it is mandatory

    //for radio validaton if it is mandatory
    if (funexit != false) {
        var radiomand;
        $(".radio-mand1").each(function () {
            $(".radio-mand1").find('.radio-inline').each(function () {
                if ($(this).find('span').hasClass('checked')) {
                    radiomand = true;
                    return false;
                } else {
                    radiomand = false;
                }
            });
            if (radiomand == false) {
                var lablename = $(this).parent().parent().find('.control-label').text();
                fieldSenseTosterError(lablename.substring(0, lablename.length - 1) + " should be selected");
                funexit = false;
                return false;
            }
        });
    }
    //for radio validaton  if it is mandatory


    if (funexit != false) {
        //checking empty form
        if (count == 0) {
            funexit = false;
            fieldSenseTosterError("Please fill up atleast one field");
            return false;
        }
        //checking empty form
        

        //var formData = new FormData($("form#submit_form")[0]);
        var formData = new FormData();
        var isFileFormat = true;
        formData.append('action', 'uploadImages');
        $.each($("input[type=file]"), function (i, obj) {
            $.each(obj.files, function (j, file) {
                //if(file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg' ) {
                if (file.type.indexOf("image") != 0) {
                    isFileFormat = false;
                    return false;
                }
                formData.append('fileupload[' + i + ']', file);
            })
            if (isFileFormat == false) {
                return false;
            }
        });
        if (isFileFormat == false) {
            fieldSenseTosterError("Please upload proper image file.");
            return false;
        }        
        $('#pleaseWaitDialog').modal('show');
        
        //On Demand Custom Forms
            clevertap.event.push("On Demand Custom Forms", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                "Source": "Web",
                "Account name": accountNamecookie,
                "UserRolecookie": UserRolecookie,
            });
        $.ajax({
            url: fieldSenseURL + "/CustomFormsReport/image/save",
            type: 'POST',
            headers: {
                "userToken": userToken
            },
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                if (data.errorCode == '0000') {
                    var imageUrlList = data.result;
                    for (i = 0; i < imageUrlList.length; i++) {
                        filleddata[indexListForImages[i]].customFormImage.imageURL = imageUrlList[i];
                    }
                    var formdetails = {
                        formid: formid,
                        //formName:addformName,
                        userid: loginUserId,
                        //submitTime:new Date(),
                        
                        filledData: filleddata
                    }
                    var jsonData = JSON.stringify(formdetails);
                    console.log("formdetails "+formdetails);
                    $.ajax({
                        type: "POST",
                        url: fieldSenseURL + "/CustomFormsReport", //need to change this url
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        data: jsonData,
                        asyn: true,
                        success: function (data) {
                            if (data.errorCode == '0000') {
                               for(var i =0 ;i<arrayId.length;i++)
                               {
                               $('.time_field_class' +arrayId[i]).val("");
                           }
                                fieldSenseTosterSuccess(data.errorMessage, true);
                                document.submit_form.reset();
                                App.updateUniform();
                            } else {
                                fieldSenseTosterError(data.errorMessage, true);
                            }
                            $('#pleaseWaitDialog').modal('hide');
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
                } else {
                    $('#pleaseWaitDialog').modal('hide');
                    fieldSenseTosterError(data.errorMessage, true);
                }

            },
            error: function (xhr, ajaxOptions, thrownError) {
                $('#pleaseWaitDialog').modal('hide');
                alert("in error:" + thrownError);
            }
        });
    }
}
// added by nikhil bhosale for Time field :- 28 aug 2017
     var hour =null;
          var min=null;
            var AmPm ;
         //   var diva = document.getElementById("sel");

            function setHour(fieldid) {
                
              
               // var id = document.getElementById("fieldid").value;
                hour = $('.HH'+fieldid).val();
              
                 if(min !== null)
                { setAmPm(fieldid);}
                //alert("min " + min);
               // document.getElementById("expires").value = hour + ":" + min +" "+AmPm;              
               // setAmPm()
               if(hour =='HH')
                {
                    $('.time_field_class' + fieldid).val("");
                }
               
            }
            function setMin(fieldid) {            
     
                 min = $('.MM'+fieldid).val();
                 //alert("bho"+min);
                //document.getElementById("expires").value = hour + ":" + min +" "+AmPm;
                
                if(hour !== null)
                { setAmPm(fieldid);}
                if(min =='MM')
                {
                    $('.time_field_class' + fieldid).val("");
                }
                
            }
            function setAmPm(fieldid){
//                AmPm = document.expiration.expAmPm.options[document.expiration.expAmPm.selectedIndex].value;
//                document.expiration.time.value = hour + ":" + min +" "+AmPm;
                 hour = $('.HH'+fieldid).val();
                 min = $('.MM'+fieldid).val();
                 AmPm = $('.am'+fieldid).val();
                  if(hour == 'HH')
                {
                   // fieldSenseTosterError("Select hour filled");
                    return false;
                }
                if(min == 'MM')
                {
                   // fieldSenseTosterError("Select minute filled");
                     return false;
                }
               //  alert("time_field_class" + fieldid);
               hour = $('.HH'+fieldid).val();
                min = $('.MM'+fieldid).val();
                 AmPm = $('.am'+fieldid).val();
                   // alert(AmPm);
       // var id = document.getElementById("fieldid").value;
        $('.time_field_class' + fieldid).val(hour + ":" + min +" "+AmPm);
              //  document.getElementsByClassName("time_field_class" + fieldid ).value = hour + ":" + min +" "+AmPm;
            //  document.getElementsByClassName('time_field_class' + fieldid);
  
            }
// Ended by nikhil