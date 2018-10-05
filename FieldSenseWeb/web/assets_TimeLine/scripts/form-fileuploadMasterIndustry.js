var FormFileUpload = function () {


    return {
        //main function to initiate the module
        init: function () {

            // Initialize the jQuery File Upload widget:
            $('#fileupload').fileupload({
                // Uncomment the following to send cross-domain cookies:
                //xhrFields: {withCredentials: true},                
                //                url: 'assets/plugins/jquery-file-upload/server/php/'
                url: fieldSenseURL + '/industryCategory/importIndustryCategory'
            });

            // Enable iframe cross-domain access via redirect option:
            $('#fileupload').fileupload(
                    'option',
                    'redirect',
                    window.location.href.replace(
                            /\/[^\/]*$/,
                            '/cors/result.html?%s'
                            )
                    );

            // Demo settings:
            $('#fileupload').fileupload('option', {
                url: $('#fileupload').fileupload('option', 'url'),
                // Enable image resizing, except for Android and Opera,
                // which actually support image resizing, but fail to
                // send Blob objects via XHR requests:
                disableImageResize: /Android(?!.*Chrome)|Opera/
                        .test(window.navigator.userAgent),
                maxFileSize: 5000000,
                acceptFileTypes: /(\.|\/)(gif|jpe?g|csv)$/i
            });

            // Upload server status check for browsers with CORS support:
            if ($.support.cors) {
                $.ajax({
                    //                    url: 'assets/plugins/jquery-file-upload/server/php/',
                    //                    url: 'http://localhost:8080/FieldSense/customer/importCustomer',
                    url: fieldSenseURL + '/industryCategory/importIndustryCategory',
                    type: 'HEAD'
                }).fail(function () {
                    //                    $('<div class="alert alert-danger"/>')
                    //                        .text('Upload server currently unavailable - ' +
                    //                                new Date())
                    //                        .appendTo('#fileupload');
                });
            }

            ////////////////////

            // Initialize the jQuery File Upload widget:
            $('#fileupload').fileupload({
                // Uncomment the following to send cross-domain cookies:
                //xhrFields: {withCredentials: true},
                autoUpload: false,
                disableImageResize: /Android(?!.*Chrome)|Opera/
                        .test(window.navigator.userAgent),
                maxFileSize: 5000000,
                acceptFileTypes: /(\.|\/)(gif|jpe?g|csv)$/i,
                type: "POST",
                //                url: "http://localhost:8080/FieldSense/customer/importCustomer",
                url: fieldSenseURL + '/industryCategory/importIndustryCategory',
                headers: {
                    "userToken": userToken
                },
                crossDomain: false,
                //        data : jsonData,
                cache: false,
                dataType: 'json',
                success: function (data) {
                    if (data.errorCode == 0000) {
                        if (data.errorMessage == 'Industry Category added successfully') {
                            $('#id_errorFile').html('');
                            fieldSenseTosterSuccessNoTimeOut(data.errorMessage, true);
                        } else {
                            var re = /Not enough space./gi;
                            if (data.errorMessage.search(re) == -1) {
				if(data.resultType.indexOf("No Error File")==0){
					$('#id_errorFile').html('');
		                        var template = '';
		                        template += '<div class="alert alert-danger">';
		                        template += '<strong>Error!</strong> '+data.errorMessage+'</div>';
		                        $('#id_errorFile').html(template);
		                        fieldSenseTosterSuccessNoTimeOut(data.errorMessage, true);
                                }else{
		                        var errorFileURL = data.result;
		                        errorFileURL = importErrorFileURLPath + errorFileURL;
		                        var template = '';
		                        template += '<div class="alert alert-danger">';
		                        template += '<strong>Error!</strong><a href="' + errorFileURL + '" download style="margin-right: 444px;">  Click here to download error file</a></div>';
		                        $('#id_errorFile').html(template);
		                        fieldSenseTosterSuccessNoTimeOut(data.errorMessage, true);
                                }
                            } else {
                                $('#id_errorFile').html('');
                                var template = '';
                                template += '<div class="alert alert-danger">';
                                template += '<strong>Error!</strong>Some records were not imported because there were not enough user accounts left in your account.</div>';
                                $('#id_errorFile').html(template);
                                fieldSenseTosterErrorNoTimeOut(data.errorMessage, true);
                            }
                        }
                    } else {
                        if (data.errorMessage == 'CSV file not in correct format. Please download the sample file.') {
                            $('#id_errorFile').html('');
                            fieldSenseTosterErrorNoTimeOut(data.errorMessage, true);
                        } else {
                            var re = /Not enough space./gi;
                            if (data.errorMessage.search(re) == -1) {
                                if(data.resultType.indexOf("No Error File")==0){
					$('#id_errorFile').html('');
		                        var template = '';
		                        template += '<div class="alert alert-danger">';
		                        template += '<strong>Error!</strong>'+data.errorMessage+'</div>';
		                        $('#id_errorFile').html(template);
		                        fieldSenseTosterErrorNoTimeOut(data.errorMessage, true);
                                }else{
		                        var errorFileURL = data.result;
		                        errorFileURL = importErrorFileURLPath + errorFileURL;
		                        var template = '';
		                        template += '<div class="alert alert-danger">';
		                        template += '<strong>Error!</strong><a href="' + errorFileURL + '" download style="margin-right: 444px;">  Click here to download error file</a></div>';
		                        $('#id_errorFile').html(template);
		                        fieldSenseTosterSuccessNoTimeOut(data.errorMessage, true);
                               }
                            } else {
                                $('#id_errorFile').html('');
                                var template = '';
                                template += '<div class="alert alert-danger">';
                                template += '<strong>Error!</strong>Some records were not imported because there were not enough user accounts left in your account.</div>';
                                $('#id_errorFile').html(template);
                                fieldSenseTosterErrorNoTimeOut(data.errorMessage, true);
                            }
                        }
                    }
                },
                completed:function(e){
                    $(".template-download").html('');                    
                }
            });

            // initialize uniform checkboxes  
            App.initUniform('.fileupload-toggle-checkbox');
        }

    };

}();
