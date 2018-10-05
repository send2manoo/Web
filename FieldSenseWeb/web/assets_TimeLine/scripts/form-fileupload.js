var FormFileUpload = function() {


    return {
        //main function to initiate the module
        init: function() {

            // Initialize the jQuery File Upload widget:
            $('#fileupload').fileupload({
                // Uncomment the following to send cross-domain cookies:
                //xhrFields: {withCredentials: true},
                //                url: 'assets/plugins/jquery-file-upload/server/php/'
                url: fieldSenseURL + '/customer/importCustomer'
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
                    url: fieldSenseURL + '/customer/importCustomer',
                    type: 'HEAD'
                }).fail(function() {
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
                url: fieldSenseURL + '/customer/importCustomer',
                headers: {
                    "userToken": userToken
                },
                crossDomain: false,
                //        data : jsonData,
                cache: false,
                dataType: 'json',
                success: function(data) {
                    if (data.errorCode == 0000) {
                        if (data.errorMessage == 'Customer created successfully') {
                            $('#id_errorFile').html('');
                            fieldSenseTosterSuccessNoTimeOut(data.errorMessage, true);
                        } else {
                            var errorFileURL = data.result;
                            errorFileURL = importErrorFileURLPath + errorFileURL;
                            var template = '';
                            template += '<div class="alert alert-danger">';
                            template += '<strong>Error!</strong><a href="' + errorFileURL + '" download style="margin-right: 444px;">  Click here to download error file</a></div>';
                            $('#id_errorFile').html(template);
                            fieldSenseTosterSuccessNoTimeOut(data.errorMessage, true);
                        }
                    } else {
                        if (data.errorMessage == 'CSV file not in correct format. Please download the sample file.') {
                            $('#id_errorFile').html('');
                            fieldSenseTosterErrorNoTimeOut(data.errorMessage, true);
                        } else {
                            var errorFileURL = data.result;
                            errorFileURL = importErrorFileURLPath + errorFileURL;
                            var template = '';
                            template += '<div class="alert alert-danger">';
//                        template+='<strong>Error!</strong><a href="#" style="margin-right: 444px;" onClick="return downloadErrorFile(\''+errorFileURL+'\');">  Click here to download error file</a></div>';
                            template += '<strong>Error!</strong><a href="' + errorFileURL + '" download style="margin-right: 444px;">  Click here to download error file</a></div>';
                            $('#id_errorFile').html(template);
                            fieldSenseTosterErrorNoTimeOut(data.errorMessage, true);
                        }
                    }
                }
            });

            // initialize uniform checkboxes
            App.initUniform('.fileupload-toggle-checkbox');
        }

    };

}();
