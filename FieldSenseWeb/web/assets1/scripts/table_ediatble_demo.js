/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var TableEditable = function () {
    
    var oGlobalTable;
    return {
        //main function to initiate the module
        init: function () {
            //    alert("TableEditable 1 : init called");

            function restoreRow(oTable, nRow) {
                console.log("nRow@ " + nRow);
                var aData = oTable.fnGetData(nRow);

                console.log("restoreRow# " + aData);
                var jqTds = $('>td', nRow);

                for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                    oTable.fnUpdate(aData[i], nRow, i, false);

                }

                oTable.fnDraw();
            }
                var counter = 1;
            function editRow(oTable, nRow) {
                //   alert("editRow");
                var aData = oTable.fnGetData(nRow);
                var jqTds = $('>td', nRow);
                
                
                jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
                jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="" id="id_pwd'+counter+'">';
                jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="" id="id_pwd1'+counter+'">';
                jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                //added for re-type pass
                jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                //ended
                // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';	
                jqTds[6].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2">Female</option></select>';
                jqTds[7].innerHTML = '<a class="edit" href="">Save</a> <a class="cancel" href="">Cancel</a><input type="hidden" id="id_counter" class="form-control input-small" value="' + counter + '">';
                counter++;
            }
//            editRowTemp method gets called when we add row in edit account temp
          
            function editRowCreateTemp(oTable, nRow,counter_no,pwd1,gender) {
                //   alert("editRow");
                var aData = oTable.fnGetData(nRow);
                var jqTds = $('>td', nRow);
                
                
                jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
                jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="'+pwd1+'" id="id_pwd'+counter_no+'">';
                jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="'+pwd1+'" id="id_pwd1'+counter_no+'">';
                jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                //added for re-type pass
                jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                //ended
                // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                if(gender == 1)
                { 
                    jqTds[6].innerHTML = '<select id="id_gender"><option value="1" selected>Male</option><option value="2">Female</option></select>';
                }else if(gender == 1)
                {
                    jqTds[6].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2" selected>Female</option></select>';
                }
                jqTds[7].innerHTML = '<a class="edit" href="">Save</a> <a class="cancel" href="">Cancel</a><input type="hidden" id="id_counter" class="form-control input-small" value="' + counter_no + '">';
               // counter++;
            }
            //            editRowTemp method gets called when we add row in edit account temp
              function editRowEditTemp(oTable, nRow,no_of_rows) {
                //   alert("editRow");
                var aData = oTable.fnGetData(nRow);
                var jqTds = $('>td', nRow);
                
                
                jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
                jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="" id="id_pwd'+no_of_rows+'">';
                jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="" id="id_pwd1'+no_of_rows+'">';
                jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                //added for re-type pass
                jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                //ended
                // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';	
                jqTds[6].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2">Female</option></select>';
                jqTds[7].innerHTML = '<a class="edit1" href="">Save</a> <a class="cancel1" href="">Cancel</a><input type="hidden" id="id_counter_i" class="form-control input-small" value="' + no_of_rows + '">';
                
            }
            //this editRowEdit is used to edit a rows in edit accont temp :- 
            function editRowEdit(oTable, nRow,no_of_rows,pwd1,gender) {
                //   alert("editRow");
                var aData = oTable.fnGetData(nRow);
                var jqTds = $('>td', nRow);
                
                
                jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
                jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="'+pwd1+'" id="id_pwd'+no_of_rows+'">';
                jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="'+pwd1+'" id="id_pwd1'+no_of_rows+'">';
                jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                //added for re-type pass
                jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                //ended
                // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                if(gender == 1)
                {
                jqTds[6].innerHTML = '<select id="id_gender"><option value="1" selected>Male</option><option value="2">Female</option></select>';
                }
                else
                {
                    jqTds[6].innerHTML = '<select id="id_gender"><option value="1" >Male</option><option value="2" selected>Female</option></select>';
                }
                jqTds[7].innerHTML = '<a class="edit1" href="">Save</a> <a class="cancel1" href="">Cancel</a><input type="hidden" id="id_counter_i" class="form-control input-small" value="' + no_of_rows + '">';
                
            }
            
            
            function editRow1(oTable, nRow1) {
                //    alert("editRow 2");
                var aData = oTable.fnGetData(nRow1);
//                console.log("oTable %%"+oTable);
//                console.log("nRow "+nRow1);
//                console.log("aData# "+aData.toString());
                var jqTds = $('>td', nRow1);
                  var pwd = $(this).closest('tr,input').find('#id_pwd').val();
                console.log("closest edit walla pwd@" +pwd);
                
                
                //console.log("gender mila"+aData[8]);

                jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
//                jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="'+ aData[2]+'">';
//                jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="'+aData[3]+'">';
                 jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="' + aData[2].valueOf() + '">';
                  jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="'+aData[3]+'">';
                 console.log("value of# "+aData[2].valueOf());
                //  jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
                jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
               //jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';	
               // jqTds[5].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2">Female</option></select>';
               jqTds[6].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2">Female</option></select>';
                jqTds[7].innerHTML = '<a class="edit1" href="">Save</a> <a class="cancel1" href="">Cancel</a>';
                
               // jqTds[6].innerHTML = '';
            }
            
             function editRow2(oTable, nRow1,pwd,gender) {
                //    alert("editRow 2");
                var aData = oTable.fnGetData(nRow1);
//                console.log("oTable %%"+oTable);
//                console.log("nRow "+nRow1);
//                console.log("aData# "+aData.toString());
                console.log("pwd in editRow"+pwd);
                var jqTds = $('>td', nRow1);
                console.log("got gender "+gender);
                  
                jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
//                jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="'+ aData[2]+'">';
//                jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="'+aData[3]+'">';
                 jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="' + pwd + '" id="id_pwd">';
                  jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="'+pwd+'" id="id_pwd1">';
                jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
               //jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';	
               // jqTds[5].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2">Female</option></select>';
               if(gender == 1)
               {  
               jqTds[6].innerHTML = '<select id="id_gender"><option value="1" selected>Male</option><option value="2">Female</option></select>';
           }else if(gender ==0)
           {
               jqTds[6].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2" selected>Female</option></select>';
               
           }
                jqTds[7].innerHTML = '<a class="edit1" href="">Save</a> <a class="cancel1" href="">Cancel</a>';
               // jqTds[6].innerHTML = '';
            }
            
            function AddRowInEdit(oTable, nRow1) {
                //    alert("editRow 2");
                var aData = oTable.fnGetData(nRow1);
//                console.log("oTable %%"+oTable);
//                console.log("nRow "+nRow1);
//                console.log("aData# "+aData.toString());
                var jqTds = $('>td', nRow1);

                jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
                jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[2] + '">';
                jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[3] + '">';
               //jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';	
               // jqTds[5].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2">Female</option></select>';
               jqTds[4].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2">Female</option></select>';
                jqTds[5].innerHTML = '<a class="edit1" href="">Save</a> <a class="cancel1" href="">Cancel</a>';
                
            }

//            function saveRow(oTable, nRow) {
//                var jqInputs = $('input', nRow);
//                var condition = true;
//
//                if (jqInputs[0].value == "")
//                {
//                    condition = false;
//                    fieldSenseTosterError("first name can't be empty .", true);
//                }
//                var uemail = jqInputs[1].value.trim();
//                if (uemail.length == 0) {
//                    condition = false;
//                    fieldSenseTosterError("Email cannot be empty", true);
//                    return false;
//                }
//                if (uemail.length > 100) {
//                    condition = false;
//                    fieldSenseTosterError("Email address can not be more than 100 characters", true);
//                    return false;
//                }
//                var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
//                if (!emailPattern.test(uemail.trim())) {
//                    condition = false;
//                    fieldSenseTosterError("Invalid email address .", true);
//                    return false;
//                }
//                var password = jqInputs[2].value.trim();
//                if (password.length == 0) {
//                    condition = false;
//                    fieldSenseTosterError("Password cannot be empty", true);
//                    return false;
//                }
//                if (password.length > 20) {
//                    condition = false;
//                    fieldSenseTosterError("Password can not be more than 20 characters", true);
//                    return false;
//                }
//                if (password.length < 8) {
//                    condition = false;
//                    fieldSenseTosterError("Password can not be less than 8 characters", true);
//                    return false;
//                }
//                var designation = jqInputs[3].value.trim();
//                if (designation.length == 0) {
//                    condition = false;
//                    fieldSenseTosterError("Please enter designation ", true);
//                    return false;
//                }
//                if (designation.length > 50) {
//                    condition = false;
//                    fieldSenseTosterError("Designation can not be more than 50 characters", true);
//                    return false;
//                }
//
//                var uMob = jqInputs[4].value;
//                if (uMob.length == 0) {
//                    condition = false;
//                    fieldSenseTosterError("Mobile No cannot be blank", true);
//                    return false;
//                }
//                var isphNo1 = /^\d+$/.test(uMob);
//                if (!isphNo1) {
//                    condition = false;
//                    fieldSenseTosterError("Invalid Mobile number.", true);
//                    return false;
//                }
//                if (uMob.length > 20) {
//                    condition = false;
//                    fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
//                    return false;
//                }
//                //  alert("email@##@ "+jqInputs[1].value);
//                if (condition)
//                {
//
//                    oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
//                    oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
//                    oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
//                    oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
//                    oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
//                    //   oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);				
//                    oTable.fnUpdate('<a class="edit" href="">Edit</a> <a class="delete" href="">Delete</a>', nRow, 6, false);
//                    oTable.fnDraw();
//                }
//            }

            function saveRow(oTable, nRow,counter_no,username,adm_email,designation,mobile_number) {
                console.log("save clicked");
                var jqInputs = $('input', nRow);
                var condition = true;

                console.log("f_name " + jqInputs[0].value);
//                if (jqInputs[0].value == "")
//                {
//                    condition = false;
//                    fieldSenseTosterError("first name can't be empty .", true);
//                    return false;
//                }
//                console.log("uemail " + jqInputs[1].value);
//                var uemail = jqInputs[1].value;
//                if (uemail.length == 0) {
//                    condition = false;
//                    fieldSenseTosterError("Email cannot be empty", true);
//                     return false;
//                }
//                if (uemail.length > 100) {
//                    condition = false;
//                    fieldSenseTosterError("Email address can not be more than 100 characters", true);
//                    return false;
//                }
//                var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
//                if (!emailPattern.test(uemail.trim())) {
//                    condition = false;
//                    fieldSenseTosterError("Invalid email address .", true);
//                    return false;
//                }
//                console.log("password " + jqInputs[2].value);
//                var password = jqInputs[2].value;
//                if (password.length == 0) {
//                    condition = false;
//                    fieldSenseTosterError("Password cannot be empty", true);
//                     return false;
//                }
//                if (password.length > 20) {
//                    condition = false;
//                    fieldSenseTosterError("Password can not be more than 20 characters", true);
//                     return false;
//                }
//                if (password.length < 8) {
//                    condition = false;
//                    fieldSenseTosterError("Password can not be less than 8 characters", true);
//                    return false;
//                }
//                var repassword = jqInputs[3].value;
//                if(password != repassword)
//                {
//                    condition = false;
//                    fieldSenseTosterError("Confirm password does not match with password .", true);
//                    return false;
//                }
//
//                
//                console.log("designation " + jqInputs[4].value);
//                var designation =jqInputs[4].value;
//                if (designation.length > 20)
//                {
//                    condition = false;
//                    fieldSenseTosterError("Designation can not be more than 20 characters", true);
//                }
//                 console.log("mobile " + jqInputs[5].value);
//                var uMob = jqInputs[5].value;
//                if (uMob.length == 0) {
//                    condition = false;
//                    fieldSenseTosterError("Mobile No cannot be blank", true);
//                    return false;
//                }
//               // console.log("password "+jqInputs[2].value);
//                var isphNo1 = /^\d+$/.test(uMob);
//                if (!isphNo1) {
//                    condition = false;
//                    fieldSenseTosterError("Invalid Mobile number.", true);
//                    return false;
//                }
//                if (uMob.length > 20) {
//                    condition = false;
//                    fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
//                    return false;
//                }
               
                //  alert("email@##@ "+jqInputs[1].value);
                if (condition)
                {
                    oTable.fnUpdate(username, 0, false);
                    oTable.fnUpdate(adm_email, 1, false);
//                    oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
//                    oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                    document.getElementById("id_pwd"+counter_no).readOnly = true;
                    //console.log("passsss "+document.getElementById('id_pwd').value);
                    document.getElementById("id_pwd1"+counter_no).readOnly = true;
                    oTable.fnUpdate(designation, 4, false);
                    oTable.fnUpdate(mobile_number, 5, false);
                    //   oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);				
                    oTable.fnUpdate('<a class="edit" href="">Edit</a> <a class="delete" href="">Delete</a> <input type="hidden" id="id_counter" class="form-control input-small" value="' + counter_no + '">', nRow, 7, false);
                    oTable.fnDraw();
                }
            }

            function saveRow1(oTable, nRow,counter_no) {
                //alert("save1");
                var jqInputs = $('input', nRow);
                var condition = true;

                if (jqInputs[0].value == "")
                {
                    condition = false;
                    fieldSenseTosterError("Username can't be blank .", true);
                }
                var uemail = jqInputs[1].value;
                if (uemail.length == 0) {
                    condition = false;
                    fieldSenseTosterError("Email cannot be blank", true);
                    return false;
                }
                if (uemail.length > 100) {
                    condition = false;
                    fieldSenseTosterError("Email address can not be more than 100 characters", true);
                    return false;
                }
                var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
                if (!emailPattern.test(uemail.trim())) {
                    condition = false;
                    fieldSenseTosterError("Invalid email address .", true);
                    return false;
                }
                  var password = jqInputs[2].value;
                if (password.length == 0) {
                    condition = false;
                    fieldSenseTosterError("Password cannot be empty", true);
                     return false;
                }
                if (password.length > 20) {
                    condition = false;
                    fieldSenseTosterError("Password can not be more than 20 characters", true);
                     return false;
                }
                if (password.length < 8) {
                    condition = false;
                    fieldSenseTosterError("Password can not be less than 8 characters", true);
                    return false;
                }
                var repassword = jqInputs[3].value;
                if(password != repassword)
                {
                    condition = false;
                    fieldSenseTosterError("Confirm password does not match with password .", true);
                    return false;
                }
                
                
                var designation = jqInputs[4].value;
                if (designation.length > 20)
                {
                    condition = false;
                    fieldSenseTosterError("Designation can not be more than 20 characters", true);
                }
                var uMob = jqInputs[5].value;
                if (uMob.length == 0) {
                    condition = false;
                    fieldSenseTosterError("Mobile No cannot be blank", true);
                    return false;
                }
                var isphNo1 = /^\d+$/.test(uMob);
                if (!isphNo1) {
                    condition = false;
                    fieldSenseTosterError("Invalid Mobile number.", true);
                    return false;
                }
                if (uMob.length > 20) {
                    condition = false;
                    fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
                    return false;
                }
                //  alert("email@##@ "+jqInputs[1].value);
                if (condition)
                {
                    
                    oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                    oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                    document.getElementById('id_pwd'+counter_no).readOnly = true;
                    //console.log("passsss "+document.getElementById('id_pwd').value);
                    document.getElementById('id_pwd1'+counter_no).readOnly = true;
                    //oTable.fnUpdate(jqInputs[2], nRow, 2, false);
                   // oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                     oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
                       oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);				
                    oTable.fnUpdate('<a class="edit1" href="">Edit</a>   <a class="delete1" href="">Delete</a> <input type="hidden" id="id_counter_i" class="form-control input-small" value="' + counter_no + '">', nRow, 7, false);
                    oTable.fnDraw();
                }
            }


            function cancelEditRow(oTable, nRow) {
                var jqInputs = $('input', nRow);
                oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
                oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
//                oTable.fnUpdate(jqInputs[2].value, nRow, 4, false);
//                oTable.fnUpdate(jqInputs[3].value, nRow, 5, false);
                oTable.fnUpdate('<a class="edit" href="">Edit</a>', nRow, 5, false);
                oTable.fnDraw();
            }
            
//            var oTable = $('#sample_editable_1').DataTable({
//                    
//                    destroy : true
//                });
//            var oTable;
//            
//            if (oTable) oTable.clear();
//            console.log("after datatable creation "+oTable);
            
            var oTable;
            oTable = $('#sample_editable_1').dataTable({    
                
                "bDestroy": true,
                "aLengthMenu": [
                    [5, 15, 20, -1],
                    [5, 15, 20, "All"] // change per page values here
                ],
                // set the initial value
                "iDisplayLength": 5,
                "sPaginationType": "bootstrap",
                "oLanguage": {
                    "sLengthMenu": "_MENU_ records",
                    "oPaginate": {
                        "sPrevious": "Prev",
                        "sNext": "Next"
                    }
                },
                "aoColumnDefs": [{
                        'bSortable': false,
                        'aTargets': [0,1,2,3,4,5,6,7]
                        
                    }
                ]
            });
            //  alert("just testing if this is printed");

            jQuery('#sample_editable_1_wrapper .dataTables_filter input').addClass("form-control input-medium"); // modify table search input
            jQuery('#sample_editable_1_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
            jQuery('#sample_editable_1_wrapper .dataTables_length select').select2({
                showSearchInput: false //hide search box with special css class
            }); // initialize select2 dropdown

            var nEditing = null;
            var nEditing1 = null;

            //function myFunction(){alert("myfunction");}

            $('#sample_editable_1_new').click(function (e) {
                // alert("sample_editable_1_new");
                e.preventDefault();
                var aiNew = oTable.fnAddData(['', '', '', '', '', '',
                    '<a class="edit" href="">Edit</a>', '<a class="cancel" data-mode="new" href="">Cancel</a>'
                ]);
                var nRow = oTable.fnGetNodes(aiNew[0]);
                editRow(oTable, nRow);
                nEditing = nRow;
            });

            $('#sample_editable_1_new1').click(function (e) {
                //alert("sample_editable_1_new1");
                e.preventDefault();
//                var aiNew = oTable.fnAddData(['', '', '', '', '', '',
//                    '<a class="edit1" href="">Edit</a>', '<a class="cancel" data-mode="new" href="">Cancel</a>'
//                ]);

                     var aiNew = oTable.fnAddData(['', '', '', '', '', '',
                    '','',''
                ]);
                var nRow1 = oTable.fnGetNodes(aiNew[0]);
                console.log("no. of rows "+oTable.fnGetData().length);
                var no_of_rows = oTable.fnGetData().length;
                editRowEditTemp(oTable, nRow1,no_of_rows);
//AddRowInEdit(oTable, nRow);
                nEditing1 = nRow1;
            });

            $('#sample_editable_1 a.delete').live('click', function (e) {
                e.preventDefault();

                if (confirm("Are you sure to delete this row ?") == false) {
                    return;
                }
//                var id = $(this).closest('tr').find('#id_admin_id').text()
//                console.log("closest id "+id);
                
                var nRow = $(this).parents('tr')[0];
                oTable.fnDeleteRow(nRow);
                // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
            });

//             $('#sample_editable_1 a.delete1').live('click', function (e) {
//                e.preventDefault();
//
//                if (confirm("Are you sure to delete this row ?") == false) {
//                    return;
//                }
//                var id = $(this).closest('tr').find('#id_admin_id').text()
//                console.log("closest id "+id);
//                var pwd = $(this).closest('tr,input').find('#id_pwd').val();
//                console.log("closest pwd@" +pwd);
//                
//                 var pwd = $(this).closest('tr,input').find('#id_pwd1').val();
//                console.log("closest pwd1@" +pwd);
//
//                
//                var nRow = $(this).parents('tr')[0];
//                oTable.fnDeleteRow(nRow);
//                // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
//            });
            
            $('#sample_editable_1 a.delete1').live('click', function (e) {
                e.preventDefault();

                if (confirm("Are you sure to delete this row ?") == false) {
                    return;
                }
                var id = $(this).closest('tr').find('#id_admin_id').text();
                console.log("closest id "+id);
                
                var pwd = $(this).closest('tr,input').find('#id_pwd').val();
                console.log("closest pwd@" +pwd);
                
                 var pwd = $(this).closest('tr,input').find('#id_pwd1').val();
                console.log("closest pwd1@" +pwd);
                var nRow = $(this).parents('tr')[0];
                $('#pleaseWaitDialog').modal('show');
//                deleteAdminUser(id);
               $.ajax({
        type: "DELETE",
        url: fieldSenseURL +"/account/userAdmin/deleteAdminUser/"+id,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken,
        },
        crossDomain: false,
        cache: false,
       // data: jsonData,
       // dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                fieldSenseTosterSuccess(data.errorMessage, true);
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                //$('#responsive2').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);    
            }
        }
        
    });
                    
               oTable.fnDeleteRow(nRow);
                
                // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
               // deleteAdminUser(id);
            });

            $('#sample_editable_1 a.cancel').live('click', function (e) {
                e.preventDefault();
//                if ($(this).attr("data-mode") == "new") {
                if (this.innerHTML == "Cancel") {
                    var nRow = $(this).parents('tr')[0];
//                    oTable.cancelEditRow(nRow);
                         cancelEditRow(nRow);
                } else {
//                    alert("oTable%^& "+oTable);
//                    alert("nEditing+++ "+nEditing);
//                    alert("inside cancel row");
                    restoreRow(oTable, nEditing);
                    nEditing = null;
                }
            });
            $('#sample_editable_1 a.cancel1').live('click', function (e) {
                e.preventDefault();
                if ($(this).attr("data-mode") == "new") {
                    var nRow = $(this).parents('tr')[0];
                    oTable.fnDeleteRow(nRow);
                } else {
//                    alert("oTable%^& "+oTable);
//                    alert("nEditing+++ "+nEditing1);
//                    alert("inside cancel row");
                    restoreRow(oTable, nEditing1);
                    nEditing1 = null;
                }
            });


            $('#sample_editable_1 a.edit').live('click', function (e) {
                e.preventDefault();
//alert("live edit");
                /* Get the row as a parent of the link that was clicked on */
                var username = $(this).closest('tr,input').find('#id_uname').val();
                console.log("username " +username);
                
                var adm_email = $(this).closest('tr,input').find('#id_adm_email').val();
                console.log("adm_email " +adm_email);
                
                var designation = $(this).closest('tr,input').find('#id_designation').val();
                console.log("designation " +designation);
                
                 var mobile_number = $(this).closest('tr,input').find('#id_mobile_number').val();
                console.log("mobile_number " +mobile_number);
                var nRow = $(this).parents('tr')[0];

               console.log("this.innerHTML "+this.innerHTML);
                if(this.innerHTML == "Save"){
                    console.log("this in save .innerHTML "+this.innerHTML);
                    nEditing = nRow;
                }
                if(this.innerHTML == "Edit")
                {
                   nEditing = null; 
                }
                console.log("this.innerHTML "+this.innerHTML);
                if (nEditing !== null && nEditing != nRow) {
                    /* Currently editing - but not this row - restore the old before continuing to edit mode */
                    restoreRow(oTable, nEditing);
                    editRow(oTable, nRow);
                    nEditing = nRow;
                } else if (nEditing == nRow && this.innerHTML == "Save") {
                    /* Editing this row and want to save it */
                    var counter_no = $(this).closest('tr,input').find('#id_counter').val();
                    console.log("save counter_no "+counter_no);
//                console.log("closest save counter " +counter_no);
//                console.log("password id :-"+"id_pwd"+counter_no);
//                console.log("Re-password id :-"+"id_pwd1"+counter_no);
                    if (saveRow(oTable, nEditing,counter_no,username,adm_email,designation,mobile_number))
                    {
                        nEditing = null;
                        //alert("Updated! Do not forget to do some ajax to sync with backend :)");
                    }
                } else {
                    /* No edit in progress - let's start one */
                     var counter_no = $(this).closest('tr,input').find('#id_counter').val();
                     console.log("pwd counter_no "+counter_no);
//                     var pwd1 = $(this).closest('tr,input').find('#id_pwd'+counter_no).val();
//                     var pwd2 = $(this).closest('tr,input').find('#id_pwd2'+counter_no).val();
//                     console.log("pwd1 "+pwd1);
//                     console.log("pwd2 "+pwd2);
                   // editRow(oTable, nRow);
                   var pwd1 = $(this).closest('tr,input').find('#id_pwd'+counter_no+'').val();
                console.log("create temp pass pwd@123 " +pwd1);
                 var gender = $(this).closest('tr,input').find('#id_gender').val();
                console.log("create account gender" +gender);
                    editRowCreateTemp(oTable, nRow,counter_no,pwd1,gender);
                    nEditing = nRow;
                }
            });
            
           
            $('#sample_editable_1 a.edit1').live('click', function (e) {
                e.preventDefault();
//alert(" edit 1");
                /* Get the row as a parent of the link that was clicked on */
               var counter_no = $(this).closest('tr,input').find('#id_counter_i').val();
                    console.log("save counter_no of edit temp "+counter_no);
                var nRow1 = $(this).parents('tr')[0];
                console.log("nRow1 edit1 @@ " + nRow1.toString());
                 if(this.innerHTML == "Edit"){
                    nEditing1 = null;
                }
                 if(this.innerHTML == "Save"){
                    console.log("this in save .innerHTML "+this.innerHTML);
                    nEditing1 = nRow1;
                }

                if (nEditing1 !== null && nEditing1 != nRow1) {
                    /* Currently editing - but not this row - restore the old before continuing to edit mode */
                    restoreRow(oTable, nEditing1);
                    editRow1(oTable, nRow1);
                    nEditing1 = nRow1;
                } else if (nEditing1 == nRow1 && this.innerHTML == "Save") {
                    /* Editing this row and want to save it */
                    // saveRow1(oTable, nEditing1);
                    
                    if (saveRow1(oTable, nEditing1,counter_no))
                    {
                        nEditing1 = null;
                        //  alert("Updated! Do not forget to do some ajax to sync with backend :)");
                    }
                } else {
                    /* No edit in progress - let's start one */
                    var pwd1 = $(this).closest('tr,input').find('#id_pwd'+counter_no).val();
                console.log("edit temp pass  " +pwd1);
                  
                
                var gender = $(this).closest('tr,input').find('#id_gender_1').val();
                console.log("closest edit walla gender" +gender);
                
                    //editRow2(oTable, nRow1,pwd1,gender);
                    editRowEdit(oTable, nRow1,counter_no,pwd1,gender);
                    nEditing1 = nRow1;
                    console.log("nEditing1$$ " + nEditing1);
                }

            });
        },
        getDataTable: function () {
            //  alert("returning the fuccker!!");
            return oGlobalTable;
        }

    };

}();


