var addCounter = 1;
        var saveCounter = 0;
//var addCounter; var saveCounter;
        var saveCreateCount;
        var addRowEditTemp;
        var TableEditable = function () {

        var oGlobalTable;
                return {
                //main function to initiate the module
                init: function () {
                //    alert("TableEditable 1 : init called");
//           addCounter =1; saveCounter =0;
                function restoreRow(oTable, nRow) {
                console.log("nRow@ " + nRow);
                        var aData = oTable.fnGetData(nRow);
                        console.log("aData[0]$$ " + aData[0] + "$" + aData[1]);
                        console.log("restoreRow# " + aData);
                        var jqTds = $('>td', nRow);
                        if (aData !== null && aData[0] !== "")
                {
                for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                oTable.fnUpdate(aData[i], nRow, i, false);
                }
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
                                jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="" id="id_pwd' + counter + '">';
                                jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="" id="id_pwd1' + counter + '">';
                                jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                                //added for re-type pass
                                jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                                //ended
                                // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';	
                                jqTds[6].innerHTML = '<select id="id_gender" class="class_gender' + counter + '"><option value="1">Male</option><option value="0">Female</option></select>';
                                jqTds[7].innerHTML = '<a class="edit" href="">Save</a> <a class="cancel" data-mode="new" href="">Cancel</a><input type="hidden" id="id_counter" class="form-control input-small" value="' + counter + '">';
                                counter++;
                        }
//          
                // this method gets called when we try to edit a row present in createacc temp


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
//                "aLengthMenu": [
//                    [20, -1],
//                    [20, "All"] // change per page values here
//                ],
                        "bLengthChange": false,
                        // set the initial value
                        "iDisplayLength": 100,
                        "bPaginate": false,
                        // "sPaginationType": "bootstrap",
                        "oLanguage": {
                        "sLengthMenu": "_MENU_ records",
                                "oPaginate": {
                                "sPrevious": "Prev",
                                        "sNext": "Next"
                                }
                        },
                        "aoColumnDefs": [{
                        'bSortable': false,
                                'aTargets': [0, 1, 2, 3, 4, 5, 6, 7]

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
                        var no_of_rows = oTable.fnGetData().length;
                        addRowEditTemp = no_of_rows;
                        saveCreateCount = no_of_rows;
                        //function myFunction(){alert("myfunction");}

                        $('#sample_editable_1_new').click(function (e) {
                // alert("sample_editable_1_new");
                addCounter++;
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
               // addRowEditTemp++;
                        e.preventDefault();
//                var aiNew = oTable.fnAddData(['', '', '', '', '', '',
//                    '<a class="edit1" href="">Edit</a>', '<a class="cancel" data-mode="new" href="">Cancel</a>'
//                ]);

                        var aiNew = oTable.fnAddData(['', '', '', '', '', '',
                                '', '', ''
                        ]);
                        var nRow1 = oTable.fnGetNodes(aiNew[0]);
                        console.log("no. of rows " + oTable.fnGetData().length);
                        editRowEditTemp(oTable, nRow1, oTable.fnGetData().length);
//AddRowInEdit(oTable, nRow);
                        nEditing1 = nRow1;
                });
                        $('#sample_editable_1 ').on('click', 'a.delete', function (e) {
                e.preventDefault();
                  var table_length = oTable.fnGetData().length;
                        console.log(table_length);
                       if(table_length == 1 ||saveCounter==1) 
                       {
                           fieldSenseTosterError("Minimum one admin should be present .", true);
                           return false;
                       }
                        if (confirm("Are you sure you want to delete this row ?") == false) {
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

                        $('#sample_editable_1').on('click', 'a.delete1', function (e) {
                e.preventDefault();
                        if (confirm("Are you sure you want to delete this row ?") == false) {
                return;
                }
                var id = $(this).closest('tr').find('#id_admin_id').text();
                        console.log("closest id " + id);
                        if (id == "")
                {
                var nRow = $(this).parents('tr')[0];
                 var table_length = oTable.fnGetData().length;
                        
                       if(table_length == 1 )
                       {
                           fieldSenseTosterError("Minimum one admin should be present .", true);
                           return false;
                       }
                        oTable.fnDeleteRow(nRow);
                }

                if (id !== "")
                {
                var nRow = $(this).parents('tr')[0];
                        var pwd = $(this).closest('tr,input').find('#id_pwd').val();
                        console.log("closest pwd@" + pwd);
                        var pwd = $(this).closest('tr,input').find('#id_pwd1').val();
                        console.log("closest pwd1@" + pwd);
                        var table_length = oTable.fnGetData().length;
                        
                       if(table_length == 1 ||saveCreateCount==1) 
                       {
                           fieldSenseTosterError("Minimum one admin should be present .", true);
                           return false;
                       }
                       deleteCount(id);
                       oTable.fnDeleteRow(nRow);
                       
//                        $('#pleaseWaitDialog').modal('show');
////                deleteAdminUser(id);
//                        $.ajax({
//                        type: "DELETE",
//                                url: fieldSenseURL + "/account/userAdmin/deleteAdminUser/" + id,
//                                contentType: "application/json; charset=utf-8",
//                                headers: {
//                                "userToken": userToken,
//                                },
//                                crossDomain: false,
//                                cache: false,
//                                // data: jsonData,
//                                // dataType: 'json',
//                                success: function (data) {
//                                if (data.errorCode == '0000') {
//                                oTable.fnDeleteRow(nRow);
//                                        fieldSenseTosterSuccess(data.errorMessage, true);
//                                        $('#pleaseWaitDialog').modal('hide');
//                                } else {
//                                $('#pleaseWaitDialog').modal('hide');
//                                        fieldSenseTosterError(data.errorMessage, true);
//                                        //$('#responsive2').modal('hide');
//                                        FieldSenseInvalidToken(data.errorMessage);
//                                }
//                                }
//
//                        });
                }


                // alert("Deleted! Do not forget to do some ajax to sync with backend :)");
                // deleteAdminUser(id);
                });
                        $('#sample_editable_1').on('click', 'a.cancel', function (e) {
                e.preventDefault();
                 var nRow = $(this).parents('tr')[0];
                 var aData = oTable.fnGetData(nRow);
                 console.log("aData $$ "+aData[0]);
//                if ($(this).attr("data-mode") == "new") {
                        if ($(this).attr("data-mode") == "new") {
                var nRow = $(this).parents('tr')[0];
                        oTable.fnDeleteRow(nRow);
                        addCounter--;
                } else {
//                    alert("oTable%^& "+oTable);
//                    alert("nEditing+++ "+nEditing);
//                    alert("inside cancel row");
                if(aData[0] !=='<input name="" value="" size="10" class="form-control input-small" id="id_uname">')
                    {  
                var nRow = $(this).parents('tr')[0];
                console.log("cancel row "+nRow);
                        restoreRow(oTable, nRow);
                        saveCounter++;
                        nEditing = null;
                    }       
                }
                });
                        $('#sample_editable_1').on('click', 'a.cancel1', function (e) {
                e.preventDefault();
                        if ($(this).attr("data-mode") == "new") {

                var nRow = $(this).parents('tr')[0];
                        oTable.fnDeleteRow(nRow);
                        addRowEditTemp--;
                        console.log("on cancel addRowEditTemp "+addRowEditTemp);
                } else {
//                    alert("oTable%^& "+oTable);
//                    alert("nEditing+++ "+nEditing1);
//                    alert("inside cancel row");
                var nRow = $(this).parents('tr')[0];
                        console.log("cancel walla oTable" + oTable + nEditing1);
                        restoreRow(oTable, nRow);
                        saveCreateCount++;
                        nEditing1 = null;
                }
                });
//            $('#sample_editable_1 a.edit').live('click', function (e) {
                        $('#sample_editable_1').on('click', 'a.edit', function (e) {
                e.preventDefault();
//alert("live edit");
                        /* Get the row as a parent of the link that was clicked on */
                        var nRow = $(this).parents('tr')[0];
                        console.log("this.innerHTML " + this.innerHTML);
                         var counter_no = $(this).closest('tr,input').find('#id_counter').val();
                        console.log("pwd counter_no " + counter_no);
                         var pwd1 = $(this).closest('tr,input').find('#id_pwd' + counter_no + '').val();
                        console.log("create temp pass pwd@123 " + pwd1);
                        var gender = $(this).closest('tr,input').find('#id_gender').val();
                        console.log("create account gender" + gender);
                        
                        if (this.innerHTML == "Save") {
                console.log("this in save .innerHTML " + this.innerHTML);
                        nEditing = nRow;
                }
                if (this.innerHTML == "Edit")
                {
                nEditing = null;
                }
                console.log("this.innerHTML " + this.innerHTML);
                        if (nEditing !== null && nEditing != nRow) {
                /* Currently editing - but not this row - restore the old before continuing to edit mode */
                restoreRow(oTable, nEditing);
                        editRow(oTable, nRow);
                        nEditing = nRow;
                } else if (nEditing == nRow && this.innerHTML == "Save") {
                /* Editing this row and want to save it */
                var counter_no = $(this).closest('tr,input').find('#id_counter').val();
                        console.log("save counter_no " + counter_no);
//                console.log("closest save counter " +counter_no);
//                console.log("password id :-"+"id_pwd"+counter_no);
//                console.log("Re-password id :-"+"id_pwd1"+counter_no);

                        if (saveRow(oTable, nEditing, counter_no,pwd1,gender))
                {
                nEditing = null;
                        //alert("Updated! Do not forget to do some ajax to sync with backend :)");
                }
                } else {
                /* No edit in progress - let's start one */
               
//                     var pwd1 = $(this).closest('tr,input').find('#id_pwd'+counter_no).val();
//                     var pwd2 = $(this).closest('tr,input').find('#id_pwd2'+counter_no).val();
//                     console.log("pwd1 "+pwd1);
//                     console.log("pwd2 "+pwd2);
                        // editRow(oTable, nRow);
                       
                        var nRow1 = $(this).parents('tr')[0];
                        editRowCreateTemp(oTable, nRow1, counter_no, pwd1, gender);
//                           $('#id_gender').css({
//                       'text-transform': 'none',
//                });
      
                        nEditing = nRow;
                }
                });
                        $('#sample_editable_1').on('click', 'a.edit1', function (e) {
                e.preventDefault();
//alert(" edit 1");
                        /* Get the row as a parent of the link that was clicked on */
                        var counter_no = $(this).closest('tr,input').find('#id_counter_i').val();
                        console.log("save counter_no of edit temp " + counter_no);
                        var nRow1 = $(this).parents('tr')[0];
                        console.log("nRow1 edit1 @@ " + nRow1.toString());
                        var gender = $(this).closest('tr,input').find('#id_gender_1').val();
                        console.log("closest edit walla gender1" + gender);
                         var pwd1 = $(this).closest('tr,input').find('#id_pwd' + counter_no).val();
                        
                        console.log("edit temp pass1  " + pwd1);
                        if (this.innerHTML == "Edit") {
                nEditing1 = null;
                }
                if (this.innerHTML == "Save") {
                console.log("this in save .innerHTML " + this.innerHTML);
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

                if (saveRow1(oTable, nEditing1, counter_no,gender,pwd1,counter_no))
                {
                nEditing1 = null;
                        //  alert("Updated! Do not forget to do some ajax to sync with backend :)");
                }
                } else {
                /* No edit in progress - let's start one */
               
//                  var uname = $(this).closest('tr,input').find('#id_uname').val();;
//                console.log("edit temp uname  " +uname);
                       
                        //editRow2(oTable, nRow1,pwd1,gender);
                        editRowEdit(oTable, nRow1, counter_no, pwd1, gender);
                         
         
                        nEditing1 = nRow1;
                        console.log("nEditing1$$ " + nEditing1);
                }

                });
                        function editRowCreateTemp(oTable, nRow1, counter_no, pwd1, gender) {
                        //   alert("editRow");
                       // var nRow1 = $(this).parents('tr')[0];
                       console.log("editRowCreateTemp gender "+gender);
                        var aData = oTable.fnGetData(nRow1);
                                var jqTds = $('>td', nRow1);
                                console.log("jqTds++ "+jqTds);
                                console.log("aData** " + aData);
                                if (aData !== null)
                        {
                        jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                                jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
                                jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="' + pwd1 + '" id="id_pwd' + counter_no + '">';
                                jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="' + pwd1 + '" id="id_pwd1' + counter_no + '">';
                                jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                                //added for re-type pass
                                jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                                //ended
                                // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                                if (gender == 1)
                        {
                            
                        jqTds[6].innerHTML = '<select id="id_gender" class="class_gender' + counter_no + '"><option value="1" selected>Male</option><option value="0">Female</option></select>';
                       // $('.class_gender').attr("disabled", false); 
                        
                        } else if (gender == 2)
                        {
                        jqTds[6].innerHTML = '<select id="id_gender" class="class_gender' + counter_no + '"><option value="1">Male</option><option value="0" selected>Female</option></select>';
                      // $('.class_gender').attr("disabled", false); 
                        }
                        jqTds[7].innerHTML = '<a class="edit" href="">Save</a> <a class="cancel" href="">Cancel</a><input type="hidden" id="id_counter" class="form-control input-small" value="' + counter_no + '">';
                                saveCounter--;
                                console.log("saveCounter edit " + saveCounter);
                        }// counter++;
                         
                        }
                //            editRowTemp method gets called when we add row in edit account temp
                function editRowEditTemp(oTable, nRow, no_of_rows) {
                //   alert("editRow");

                var aData = oTable.fnGetData(nRow);
                        var jqTds = $('>td', nRow);
                        jqTds[0].innerHTML = '<input type="text" id="id_uname" class="form-control input-small" value="' + aData[0] + '">';
                        jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
                        jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="" id="id_pwd' + no_of_rows + '">';
                        jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="" id="id_pwd1' + no_of_rows + '">';
                        jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                        //added for re-type pass
                        jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                        //ended
                        // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';	
                        jqTds[6].innerHTML = '<select id="id_gender_1" class="class_editGender1' + no_of_rows + '"><option value="1">Male</option><option value="2">Female</option></select>';
                        jqTds[7].innerHTML = '<a class="edit1" href="">Save</a> <a class="cancel1" data-mode="new" href="">Cancel</a><input type="hidden" id="id_counter_i" class="form-control input-small" value="' + no_of_rows + '">';
                       // saveCreateCount--;
                        addRowEditTemp++;
                }
                //this editRowEdit is used to edit a rows in edit accont temp :- 
                function editRowEdit(oTable, nRow, no_of_rows, pwd1, gender, uname) {
                //   alert("editRow");
                console.log("editRowEdit gender "+gender);
                var aData = oTable.fnGetData(nRow);
                console.log("aData++"+aData);
                        var jqTds = $('>td', nRow);
                        if (aData !== null)
                {
                console.log("aData[0] @@ " + aData[0]);
                        // jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + uname + '">';      
                        jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                        jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
                        jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="' + pwd1 + '" id="id_pwd' + no_of_rows + '">';
                        jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="' + pwd1 + '" id="id_pwd1' + no_of_rows + '">';
                        jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                        //added for re-type pass
                        jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                        //ended
                        // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                        if (gender == 1)
                {
                jqTds[6].innerHTML = '<select id="id_gender_1" class="class_editGender1' + no_of_rows + '"><option value="1" selected>Male</option><option value="2">Female</option></select>';
                }
                else
                {
                jqTds[6].innerHTML = '<select id="id_gender_1" class="class_editGender1' + no_of_rows + '"><option value="1" >Male</option><option value="2" selected>Female</option></select>';
                }
                jqTds[7].innerHTML = '<a class="edit1" href="">Save</a> <a class="cancel1" href="">Cancel</a><input type="hidden" id="id_counter_i" class="form-control input-small" value="' + no_of_rows + '">';
                        saveCreateCount--;
                }
                }


                function editRow1(oTable, nRow1) {
                //    alert("editRow 2");
                var aData = oTable.fnGetData(nRow1);
//                console.log("oTable %%"+oTable);
//                console.log("nRow "+nRow1);
//                console.log("aData# "+aData.toString());
                        var jqTds = $('>td', nRow1);
                        var pwd = $(this).closest('tr,input').find('#id_pwd').val();
                        console.log("closest edit walla pwd@" + pwd);
                        //console.log("gender mila"+aData[8]);

                        jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                        jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
//                jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="'+ aData[2]+'">';
//                jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="'+aData[3]+'">';
                        jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="' + aData[2].valueOf() + '">';
                        jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="' + aData[3] + '">';
                        console.log("value of# " + aData[2].valueOf());
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

                function editRow2(oTable, nRow1, pwd, gender) {
                //    alert("editRow 2");
                var aData = oTable.fnGetData(nRow1);
//                console.log("oTable %%"+oTable);
//                console.log("nRow "+nRow1);
//                console.log("aData# "+aData.toString());
                        console.log("pwd in editRow" + pwd);
                        var jqTds = $('>td', nRow1);
                        console.log("got gender " + gender);
                        jqTds[0].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[0] + '">';
                        jqTds[1].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[1] + '">';
//                jqTds[2].innerHTML = '<input type="text" class="form-control input-small" value="'+ aData[2]+'">';
//                jqTds[3].innerHTML = '<input type="text" class="form-control input-small" value="'+aData[3]+'">';
                        jqTds[2].innerHTML = '<input type="password" class="form-control input-small" value="' + pwd + '" id="id_pwd">';
                        jqTds[3].innerHTML = '<input type="password" class="form-control input-small" value="' + pwd + '" id="id_pwd1">';
                        jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                        jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';
                        //jqTds[4].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[4] + '">';
                        // jqTds[5].innerHTML = '<input type="text" class="form-control input-small" value="' + aData[5] + '">';	
                        // jqTds[5].innerHTML = '<select id="id_gender"><option value="1">Male</option><option value="2">Female</option></select>';
                        if (gender == 1)
                {
                jqTds[6].innerHTML = '<select id="id_gender"><option value="1" selected>Male</option><option value="2">Female</option></select>';
                } else if (gender == 0)
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
                // var save =0;
                function saveRow(oTable, nRow, counter_no,pwd1,gender) {
                //console.log("save clicked");
                var jqInputs = $('input', nRow);
                        var jqSelect = $('option', nRow);
                        // save++;
                        //console.log("save no"+save);
                        var condition = true;
                        console.log("counter_no @@ " + counter_no);
                        console.log("f_name " + jqInputs[0].value);
                        if (jqInputs[0].value == "")
                {
                condition = false;
                        fieldSenseTosterError("Username can't be empty .", true)

                        return false;
                }
                console.log("uemail " + jqInputs[1].value);
                        var uemail = jqInputs[1].value;
                        if (uemail.length == 0) {
                condition = false;
                        fieldSenseTosterError("Email cannot be empty", true);
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
                console.log("password " + jqInputs[2].value);
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
                        if (password != repassword)
                {
                condition = false;
                        fieldSenseTosterError("Confirm password does not match with password .", true);
                        return false;
                }


                console.log("designation " + jqInputs[4].value);
                        var designation = jqInputs[4].value;
                        if (designation.length > 20)
                {
                condition = false;
                        fieldSenseTosterError("Designation can not be more than 20 characters", true);
                        return false;
                }
                if(designation.length == 0)
                {
                     condition = false;
                        fieldSenseTosterError("User designation can't be empty .", true);
                        return false;
                }
                console.log("mobile " + jqInputs[5].value);
                        var uMob = jqInputs[5].value;
                        if (uMob.length == 0) {
                condition = false;
                        fieldSenseTosterError("Mobile No cannot be blank", true);
                        return false;
                }
                // console.log("password "+jqInputs[2].value);
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
                console.log("before saveCounter %%" + saveCounter);
                        console.log(" before addCounter1 ##" + addCounter);
                saveCounter++;
                        console.log("saveCounter %%" + saveCounter);
                        console.log("addCounter1 ##" + addCounter);
//                      if(saveCounter !== addCounter)
//                      {
//                           condition = false;
//                    fieldSenseTosterError("save admin detail", true);
//                    return false;
//                      }
                        //  alert("email@##@ "+jqInputs[1].value);
                        if (condition)
                {
                oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                        oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                         oTable.fnUpdate('<input type="password" class="form-control input-small" value="' + pwd1 + '" id="id_pwd' + counter_no + '" readonly>', nRow, 2, false);
                        document.getElementById('id_pwd' + counter_no).readOnly = true;
                        oTable.fnUpdate('<input type="password" class="form-control input-small" value="' + pwd1 + '" id="id_pwd1' + counter_no + '" readonly>', nRow, 3, false);
                        //console.log("passsss "+document.getElementById('id_pwd').value);
                        document.getElementById('id_pwd1' + counter_no).readOnly = true;
//                    oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
//                    oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                        // '<input id="id_pwd1"'+counter_no+' class="form-control input-small" type="password" value='"++"' readonly="">'
//                        document.getElementById("id_pwd" + counter_no).readOnly = true;
//                        //console.log("passsss "+document.getElementById('id_pwd').value);
//                        document.getElementById("id_pwd1" + counter_no).readOnly = true;
                        oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
                        oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);
                      //  $('.class_gender').attr("disabled", true); 
                         console.log("saverow1 gender "+gender);
                if(gender == 1) 
                {
                 oTable.fnUpdate('<select id="id_gender" class="class_gender' + counter_no + '" disabled><option value="1" selected>Male</option><option value="2">Female</option></select>', nRow, 6, false);
                // $('.class_editGender1').attr("disabled", true); 
                }
                if(gender == 0) 
                {
                    oTable.fnUpdate('<select id="id_gender" class="class_gender' + counter_no + '" disabled><option value="1" >Male</option><option value="2" selected>Female</option></select>', nRow, 6, false);
                  //  $('.class_editGender1').attr("disabled", true); 
                }
                        // class_editGender1
//                        $('.class_gender').css({
//                        'appearance': 'none',
//                        '- webkit - appearance': 'none',
//                        '- moz - appearance': 'none',
//                        'button - border' : 'none',
//                        'border': 'none',
//                        /* needed for Firefox: */
//                        'overflow':'hidden',
//                        'width': '100 %' ,
//                        'background - color':'#FFFFFF'
//                });
                        //   oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);
                        //oTable.fnUpdate('<label for="1">Male</label>', nRow, 6, false);
                        oTable.fnUpdate('<a class="edit" href="">Edit</a> <a class="delete" href="">Delete</a> <input type="hidden" id="id_counter" class="form-control input-small" value="' + counter_no + '">', nRow, 7, false);
                        oTable.fnDraw();
                }


                }
//             var i=0;
//             var j=0;
//             console.log("i opar"+(i=i+1));
//            function saveRow(oTable, nRow,counter_no) {
//                console.log("save clicked");
//                var jqInputs = $('input', nRow);
//              //  var condition = true;
//
//                //console.log("f_name " + jqInputs[0].value);
//               
//                var k=i;
//                console.log("k value "+k);
//               // console.log("i value "+i);
//                 if (jqInputs[0].value == "")
//                {
//                    
//                    return false;
//                }if(jqInputs[0].value !== "")
//                {
//                    k=1;
//                }
//                if(k==0)
//                {
//                    fieldSenseTosterError("first name can't be empty .", true);
//                }
//                else if(k==1)
//                {
//                    fieldSenseTosterError("chal gaya badsha .", true);
//                    return;
//                }
////                if (jqInputs[0].value == "")
////                {
////                    condition = false;
////                    fieldSenseTosterError("first name can't be empty .", true)
////                    
////                    return false;
////                }
////                console.log("uemail " + jqInputs[1].value);
////                var uemail = jqInputs[1].value;
////                if (uemail.length == 0) {
////                    condition = false;
////                    fieldSenseTosterError("Email cannot be empty", true);
////                     return false;
////                }
////                if (uemail.length > 100) {
////                    condition = false;
////                    fieldSenseTosterError("Email address can not be more than 100 characters", true);
////                    return false;
////                }
////                var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
////                if (!emailPattern.test(uemail.trim())) {
////                    condition = false;
////                    fieldSenseTosterError("Invalid email address .", true);
////                    return false;
////                }
////                console.log("password " + jqInputs[2].value);
////                var password = jqInputs[2].value;
////                if (password.length == 0) {
////                    condition = false;
////                    fieldSenseTosterError("Password cannot be empty", true);
////                     return false;
////                }
////                if (password.length > 20) {
////                    condition = false;
////                    fieldSenseTosterError("Password can not be more than 20 characters", true);
////                     return false;
////                }
////                if (password.length < 8) {
////                    condition = false;
////                    fieldSenseTosterError("Password can not be less than 8 characters", true);
////                    return false;
////                }
////                var repassword = jqInputs[3].value;
////                if(password != repassword)
////                {
////                    condition = false;
////                    fieldSenseTosterError("Confirm password does not match with password .", true);
////                    return false;
////                }
////
////                
////                console.log("designation " + jqInputs[4].value);
////                var designation =jqInputs[4].value;
////                if (designation.length > 20)
////                {
////                    condition = false;
////                    fieldSenseTosterError("Designation can not be more than 20 characters", true);
////                }
////                 console.log("mobile " + jqInputs[5].value);
////                var uMob = jqInputs[5].value;
////                if (uMob.length == 0) {
////                    condition = false;
////                    fieldSenseTosterError("Mobile No cannot be blank", true);
////                    return false;
////                }
////               // console.log("password "+jqInputs[2].value);
////                var isphNo1 = /^\d+$/.test(uMob);
////                if (!isphNo1) {
////                    condition = false;
////                    fieldSenseTosterError("Invalid Mobile number.", true);
////                    return false;
////                }
////                if (uMob.length > 20) {
////                    condition = false;
////                    fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
////                    return false;
////                }
//               
//                //  alert("email@##@ "+jqInputs[1].value);
////                if (condition)
////                {
////                    oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
////                    oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
//////                    oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
//////                    oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
////                    document.getElementById("id_pwd"+counter_no).readOnly = true;
////                    //console.log("passsss "+document.getElementById('id_pwd').value);
////                    document.getElementById("id_pwd1"+counter_no).readOnly = true;
////                    oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
////                    oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);
////                    //   oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);				
////                    oTable.fnUpdate('<a class="edit" href="">Edit</a> <a class="delete" href="">Delete</a> <input type="hidden" id="id_counter" class="form-control input-small" value="' + counter_no + '">', nRow, 7, false);
////                    oTable.fnDraw();
////                }
//            }

                function  saveRow1(oTable, nRow, counter_no,gender,pwd1,counter_no) {
                //alert("save1");

                var jqInputs = $('input', nRow);
                        var condition = true;
                        if (jqInputs[0].value == "")
                {
                condition = false;
                        fieldSenseTosterError("Username can't be empty.", true);
                         return false;
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
                        if (password != repassword)
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
                         return false;
                }
                if(designation.length ==0)
                {
                     condition = false;
                        fieldSenseTosterError("User designation can't be empty .", true);
                         return false;
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
                saveCreateCount++;
                        //  alert("email@##@ "+jqInputs[1].value);
                        console.log("saveCreateCounter %%" + saveCreateCount);
                        console.log("addCreateCounter %%" + addRowEditTemp);
                        console.log("addCounter1 ##" + addCounter);
                        console.log("saveCounter%%% "+saveCounter);
                        if (condition)
                {
                console.log("counter_no saverow1 " + counter_no);
                        oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
                        // oTable.fnUpdate( '<input type="text" id="id_uname" style="border:none" class="form-control input-small" value="' + jqInputs[0].value + '" readonly>', nRow, 0, false);
                        oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
                        
                        oTable.fnUpdate('<input type="password" class="form-control input-small" value="' + pwd1 + '" id="id_pwd' + counter_no + '" readonly>', nRow, 2, false);
                        document.getElementById('id_pwd' + counter_no).readOnly = true;
                        oTable.fnUpdate('<input type="password" class="form-control input-small" value="' + pwd1 + '" id="id_pwd1' + counter_no + '" readonly>', nRow, 3, false);
                        //console.log("passsss "+document.getElementById('id_pwd').value);
                        document.getElementById('id_pwd1' + counter_no).readOnly = true;
                        //oTable.fnUpdate(jqInputs[2], nRow, 2, false);
                        // oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
                        oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
                        oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);
                        //  $('.class_editGender1').attr("disabled", true); 
                       //   document.getElementsByClassName('.class_editGender1').disabled = true;
                        // class_editGender1
//                        $('.class_editGender1').css({
//                        'appearance': 'none',
//                        '- webkit - appearance': 'none',
//                        '- moz - appearance': 'none',
//                        'button - border' : 'none',
//                        'border': 'none',
//                        /* needed for Firefox: */
//                        'overflow':'hidden',
//                        'width': '120 %' ,
//                        'background - color':'#FFFFFF'
//                });
                console.log("saverow1 gender "+gender);
                if(gender == 1) 
                {
                 oTable.fnUpdate('<select id="id_gender_1" class="class_editGender1' + counter_no + '" disabled><option value="1" selected>Male</option><option value="2">Female</option></select>', nRow, 6, false);
                // $('.class_editGender1').attr("disabled", true); 
                }
                if(gender == 2) 
                {
                    oTable.fnUpdate('<select id="id_gender_1" class="class_editGender1' + counter_no + '" disabled><option value="1" >Male</option><option value="2" selected>Female</option></select>', nRow, 6, false);
                  //  $('.class_editGender1').attr("disabled", true); 
                }
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
                },
                        getDataTable: function () {
                        //  alert("returning the fuccker!!");
                        return oGlobalTable;
                        }

                };
                }();
        function checkEditTempAdminSave(accId, userId, allowOfflineAccount_State)
                {
                if (editAccValidation())
                {
                console.log("saveCreateCount :" + saveCreateCount + " addRowEditTemp :" + addRowEditTemp);
                        if (saveCreateCount !== addRowEditTemp)
                {

                //condition = false;
                fieldSenseTosterError("save edit admin detail", true);
                        return false;
                }
                else
                {
                editAccDetails(accId, userId, allowOfflineAccount_State);
                }
                }
                }
        function checkCancel()
                {
                if (createAccountValidation())
                {
                console.log("saveCounter :" + saveCounter + "addCounter :" + addCounter);
                        if (saveCounter !== addCounter)
                {
                //condition = false;
                fieldSenseTosterError("save admin detail", true);
                        return false;
                }
                else
                {
//                      saveCounter = 0;
//                      addCounter =1;
                createAcoount(saveCounter, addCounter);
                }

                // alert("check");
                }
                }
        var errorcode;
                function verify(errorcode)
                        {
                        //  alert("errorcode@@ "+errorcode);
                        if (errorcode === '0000')
                        {
                        //alert("Inside errorcode "+errorcode);
                        saveCounter = 0;
                                addCounter = 1;
                        }

                        }

 