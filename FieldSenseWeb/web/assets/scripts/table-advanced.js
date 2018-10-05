var TableAdvanced = function () {

    var initTable1 = function() {

        /* Formatting function for row details */
        function fnFormatDetails ( oTable, nTr )
        {
            var aData = oTable.fnGetData( nTr );
            var sOut = '<table width="100%" border="0" cellspacing="2" cellpadding="2">';
			sOut += '<tr><td colspan="6"><b>Contacts</b></td></tr>';
			sOut += '<tr><td width="8%">Name:</td><td width="25%">Mr. Mahesh</td><td width="8%">Name:</td><td width="25%">Mr. Kamble</td><td width="8%">Name:</td><td width="25%">Mr. Khan</td></tr>';
			sOut += '<tr><td width="8%">Phone:</td><td width="25%">022-24578456</td><td width="8%">Phone:</td><td width="25%">022-265475458</td><td width="8%">Phone:</td><td width="25%">022-24568745</td></tr>';
			sOut += '<tr><td width="8%">Email:</td><td width="25%">abccorp@mail.in</td><td width="8%">Email:</td><td width="25%">abccorp@mail.in</td><td width="8%">Email:</td><td width="25%">abccorp@mail.in</td></tr>';
            sOut += '</table>';
             
            return sOut;
        }

        /*
         * Insert a 'details' column to the table
         */
        var nCloneTh = document.createElement( 'th' );
        var nCloneTd = document.createElement( 'td' );
        nCloneTd.innerHTML = '<span class="row-details row-details-close"></span>';
         
        $('#sample_1 thead tr').each( function () {
            this.insertBefore( nCloneTh, this.childNodes[0] );
        } );
         
        $('#sample_1 tbody tr').each( function () {
            this.insertBefore(  nCloneTd.cloneNode( true ), this.childNodes[0] );
        } );
         
        /*
         * Initialize DataTables, with no sorting on the 'details' column
         */
        var oTable = $('#sample_1').dataTable( {
            "aoColumnDefs": [
                {"bSortable": false, "aTargets": [ 0,7 ] }
            ],
            "aaSorting": [[2, 'asc']],
             "aLengthMenu": [
                [5, 10, 15, 20],
                [5, 10, 15, 20] // change per page values here
            ],
            // set the initial value
            "iDisplayLength": 10,
        });

        jQuery('#sample_1_wrapper .dataTables_filter input').addClass("form-control input-large"); // modify table search input
        jQuery('#sample_1_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
        jQuery('#sample_1_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
         
        /* Add event listener for opening and closing details
         * Note that the indicator for showing which row is open is not controlled by DataTables,
         * rather it is done here
         */
        $('#sample_1').on('click', ' tbody td .row-details', function () {
            var nTr = $(this).parents('tr')[0];
            if ( oTable.fnIsOpen(nTr) )
            {
                /* This row is already open - close it */
                $(this).addClass("row-details-close").removeClass("row-details-open");
                oTable.fnClose( nTr );
            }
            else
            {
                /* Open this row */                
                $(this).addClass("row-details-open").removeClass("row-details-close");
                oTable.fnOpen( nTr, fnFormatDetails(oTable, nTr), 'details' );
            }
        });

    }

     var initTable2 = function() {
        var oTable = $('#sample_2').dataTable( {           
            "aoColumnDefs": [
                {"bSortable": false, "aTargets": [ 6 ] }
            ],
            "aaSorting": [[2, 'asc']],
			"aLengthMenu": [
				[5, 15, 20, -1],
				[5, 15, 20, "All"] // change per page values here
			],
            // set the initial value
            "iDisplayLength": 15,
			"bFilter": false,
        });

        jQuery('#sample_2_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
        jQuery('#sample_2_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
        jQuery('#sample_2_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

        $('#sample_2_column_toggler input[type="checkbox"]').change(function(){
            /* Get the DataTables object again - this is not a recreation, just a get of the object */
            var iCol = parseInt($(this).attr("data-column"));
            var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
            oTable.fnSetColumnVis(iCol, (bVis ? false : true));
        });
    }
	
     var initTable3 = function() {
        var oTable = $('#sample_3').dataTable( {           
            "aoColumnDefs": [
                {"bSortable": false, "aTargets": [ 5 ] }
            ],
            "aaSorting": [[2, 'asc']],
             "aLengthMenu": [
                [5, 10, 15, 20],
                [5, 10, 15, 20] // change per page values here
            ],
            // set the initial value
            "iDisplayLength": 10,
			"bFilter": false,
        });

        jQuery('#sample_3_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
        jQuery('#sample_3_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
        jQuery('#sample_3_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

        $('#sample_3_column_toggler input[type="checkbox"]').change(function(){
            /* Get the DataTables object again - this is not a recreation, just a get of the object */
            var iCol = parseInt($(this).attr("data-column"));
            var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
            oTable.fnSetColumnVis(iCol, (bVis ? false : true));
        });
    }	

     var initTable31 = function() {
        var oTable = $('#sample_31').dataTable( {           
            "aoColumnDefs": [
                {"bSortable": false, "aTargets": [ 4 ] }
            ],
            "aaSorting": [[0, 'asc']],
             "aLengthMenu": [
                [5, 10, 15, 20],
                [5, 10, 15, 20] // change per page values here
            ],
            // set the initial value
            "iDisplayLength": 10,
			"bFilter": false,
        });

        jQuery('#sample_31_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
        jQuery('#sample_31_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
        jQuery('#sample_31_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

        $('#sample_3_column_toggler input[type="checkbox"]').change(function(){
            /* Get the DataTables object again - this is not a recreation, just a get of the object */
            var iCol = parseInt($(this).attr("data-column"));
            var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
            oTable.fnSetColumnVis(iCol, (bVis ? false : true));
        });
    }
	
     var initTable4 = function() {
        var oTable = $('#sample_4').dataTable( {           
            "aoColumnDefs": [
                {"bSortable": false, "aTargets": [ 2 ] }
            ],
            "aaSorting": [[2, 'asc']],
             "aLengthMenu": [
                [5, 10, 15, 20],
                [5, 10, 15, 20] // change per page values here
            ],
            // set the initial value
            "iDisplayLength": 10,
			"bFilter": false,
        });

        jQuery('#sample_4_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
        jQuery('#sample_4_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
        jQuery('#sample_42_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

        $('#sample_4_column_toggler input[type="checkbox"]').change(function(){
            /* Get the DataTables object again - this is not a recreation, just a get of the object */
            var iCol = parseInt($(this).attr("data-column"));
            var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
            oTable.fnSetColumnVis(iCol, (bVis ? false : true));
        });
    }
	
     var initTable5 = function() {
         var oTable = $('#sample_5').dataTable( {           
            "aaSorting": [[2, 'asc']],
             "aLengthMenu": [
                [-1, 5, 10, 15, 20],
                ["All", 5, 10, 15, 20] // change per page values here
            ],
            // set the initial value
            "iDisplayLength": -1,
            "bFilter": false
        });
//        var oTable = $('#dataTable_attendance').dataTable( {           
//            "aaSorting": [[2, 'asc']],
//             "aLengthMenu": [
//                [-1, 5, 10, 15, 20],
//                ["All", 5, 10, 15, 20] // change per page values here
//            ],
//            // set the initial value
//            "iDisplayLength": -1,
//            "bFilter": false
//        });
//        
//        var oTable1 = $('#dataTable_visit').dataTable( {           
//            "aaSorting": [[2, 'asc']],
//             "aLengthMenu": [
//                [-1, 5, 10, 15, 20],
//                ["All", 5, 10, 15, 20] // change per page values here
//            ],
//            // set the initial value
//            "iDisplayLength": -1,
//            "bFilter": false
//        });
//        
//        var oTable2 = $('#dataTable_expense').dataTable( {           
//            "aaSorting": [[2, 'asc']],
//             "aLengthMenu": [
//                [-1, 5, 10, 15, 20],
//                ["All", 5, 10, 15, 20] // change per page values here
//            ],
//            // set the initial value
//            "iDisplayLength": -1,
//            "bFilter": false
//        });
//        
////        var oTable3 = $('#dataTable_travel').dataTable( {           
////            "aaSorting": [[2, 'asc']],
////             "aLengthMenu": [
////                [-1, 5, 10, 15, 20],
////                ["All", 5, 10, 15, 20] // change per page values here
////            ],
////            // set the initial value
////            "iDisplayLength": -1,
////            "bFilter": false,
////        });
//        
//        var oTable4 = $('#dataTable_monthly_travel').dataTable( {           
//            "aaSorting": [[2, 'asc']],
//             "aLengthMenu": [
//                [-1, 5, 10, 15, 20],
//                ["All", 5, 10, 15, 20] // change per page values here
//            ],
//            // set the initial value
//            "iDisplayLength": -1,
//            "bFilter": false
//        });
//        
//        jQuery('#dataTable_attendance_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
//        jQuery('#dataTable_attendance_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
//        jQuery('#dataTable_attendance_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
//        
//        jQuery('#dataTable_monthly_travel_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
//       	jQuery('#dataTable_monthly_travel_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
//       	jQuery('#dataTable_monthly_travel_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
//        
//        jQuery('#dataTable_expense_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
//       	jQuery('#dataTable_expense_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
//       	jQuery('#dataTable_expense_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
//        
//        jQuery('#dataTable_visit_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
//       	jQuery('#dataTable_visit_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
//       	jQuery('#dataTable_visit_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
//        
//        jQuery('#sample_i5_wrapper .dataTables_filter input').addClass("form-control  input-large"); // modify table search input
//        jQuery('#sample_i5_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
//        jQuery('#sample_i5_wrapper .dataTables_length select').select2(); // initialize select2 dropdown
     }
	
     var initTable6 = function() {
        var oTable = $('#sample_21').dataTable( {   
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
                        'aTargets': [6]
                    }
                ]
				
				
            });
			
			

        jQuery('#sample_21_wrapper .dataTables_filter input').addClass("form-control input-medium"); // modify table search input
        jQuery('#sample_21_wrapper .dataTables_length select').addClass("form-control input-small"); // modify table per page dropdown
        jQuery('#sample_21_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

        $('#sample_21_column_toggler input[type="checkbox"]').change(function(){
            /* Get the DataTables object again - this is not a recreation, just a get of the object */
            var iCol = parseInt($(this).attr("data-column"));
            var bVis = oTable.fnSettings().aoColumns[iCol].bVisible;
            oTable.fnSetColumnVis(iCol, (bVis ? false : true));
        });
		
		
    }	
	
    return {

        //main function to initiate the module
        init: function () {
            
            if (!jQuery().dataTable) {
                return;
            }

            initTable1();
            initTable2();
			initTable3();
			initTable31();
			initTable4();
			initTable5();
			initTable6();
        }

    };

}();