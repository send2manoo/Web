var FormEditable = function () {

    $.mockjaxSettings.responseTime = 500;

    var log = function (settings, response) {
        var s = [],
            str;
        s.push(settings.type.toUpperCase() + ' url = "' + settings.url + '"');
        for (var a in settings.data) {
            if (settings.data[a] && typeof settings.data[a] === 'object') {
                str = [];
                for (var j in settings.data[a]) {
                    str.push(j + ': "' + settings.data[a][j] + '"');
                }
                str = '{ ' + str.join(', ') + ' }';
            } else {
                str = '"' + settings.data[a] + '"';
            }
            s.push(a + ' = ' + str);
        }
        s.push('RESPONSE: status = ' + response.status);

        if (response.responseText) {
            if ($.isArray(response.responseText)) {
                s.push('[');
                $.each(response.responseText, function (i, v) {
                    s.push('{value: ' + v.value + ', text: "' + v.text + '"}');
                });
                s.push(']');
            } else {
                s.push($.trim(response.responseText));
            }
        }
        s.push('--------------------------------------\n');
        $('#console').val(s.join('\n') + $('#console').val());
    }

    var initAjaxMock = function () {
        //ajax mocks

        $.mockjax({
            url: '/post',
            response: function (settings) {
                log(settings, this);
            }
        });

        $.mockjax({
            url: '/error',
            status: 400,
            statusText: 'Bad Request',
            response: function (settings) {
                this.responseText = 'Please input correct value';
                log(settings, this);
            }
        });

        $.mockjax({
            url: '/status',
            status: 500,
            response: function (settings) {
                this.responseText = 'Internal Server Error';
                log(settings, this);
            }
        });

        $.mockjax({
            url: '/groups',
            response: function (settings) {
                this.responseText = [{
                        value: 0,
                        text: 'Guest'
                    }, {
                        value: 1,
                        text: 'Service'
                    }, {
                        value: 2,
                        text: 'Customer'
                    }, {
                        value: 3,
                        text: 'Operator'
                    }, {
                        value: 4,
                        text: 'Support'
                    }, {
                        value: 5,
                        text: 'Admin'
                    }
                ];
                log(settings, this);
            }
        });

    }

    var initEditables = function () {

        //set editable mode based on URL parameter
        if (App.getURLParameter('mode') == 'inline') {
            $.fn.editable.defaults.mode = 'inline';
            $('#inline').attr("checked", true);
            jQuery.uniform.update('#inline');
        } else {
            $('#inline').attr("checked", false);
            jQuery.uniform.update('#inline');
        }

        //global settings 
        $.fn.editable.defaults.inputclass = 'form-control';
        $.fn.editable.defaults.url = '/post';

        //editables element samples 
        $('#username').editable({
            url: '/post',
            type: 'text',
            pk: 1,
            name: 'username',
            title: 'Enter username'
        });

        $('#firstname').editable({
            validate: function (value) {
                if ($.trim(value) == '') return 'This field is required';
            }
        });

        $('#sex').editable({
            prepend: "not selected",
            inputclass: 'form-control',
            source: [{
                    value: 1,
                    text: 'Male'
                }, {
                    value: 2,
                    text: 'Female'
                }
            ],
            display: function (value, sourceData) {
                var colors = {
                    "": "gray",
                    1: "green",
                    2: "blue"
                },
                    elem = $.grep(sourceData, function (o) {
                        return o.value == value;
                    });

                if (elem.length) {
                    $(this).text(elem[0].text).css("color", colors[value]);
                } else {
                    $(this).empty();
                }
            }
        });

        $('#status').editable();
		$('#addres').editable();
		$('#emp').editable();

        $('#group').editable({
            showbuttons: false
        });

        $('#vacation').editable({
            rtl : App.isRTL() 
        });

        $('#dob').editable({
            inputclass: 'form-control',
        });

        $('#event').editable({
            placement: (App.isRTL() ? 'left' : 'right'),
            combodate: {
                firstItem: 'name'
            }
        });

        $('#meeting_start').editable({
            format: 'yyyy-mm-dd hh:ii',
            viewformat: 'dd/mm/yyyy hh:ii',
            validate: function (v) {
                if (v && v.getDate() == 10) return 'Day cant be 10!';
            },
            datetimepicker: {
                rtl : App.isRTL(),
                todayBtn: 'linked',
                weekStart: 1
            }
        });

        $('#comments').editable({
            showbuttons: 'bottom'
        });

        $('#note').editable({
            showbuttons : (App.isRTL() ? 'left' : 'right')
        });

        $('#pencil').click(function (e) {
            e.stopPropagation();
            e.preventDefault();
            $('#note').editable('toggle');
        });

        $('#state').editable({
            source: ["Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Dakota", "North Carolina", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"]
        });

        $('#fruits').editable({
            pk: 1,
            limit: 3,
            source: [{
                    value: 1,
                    text: 'banana'
                }, {
                    value: 2,
                    text: 'peach'
                }, {
                    value: 3,
                    text: 'apple'
                }, {
                    value: 4,
                    text: 'watermelon'
                }, {
                    value: 5,
                    text: 'orange'
                }
            ]
        });

        $('#fruits').on('shown', function(e, reason) {
            App.initUniform();
        });

        $('#tags').editable({
            inputclass: 'form-control input-medium',
            select2: {
                tags: ['html', 'javascript', 'css', 'ajax'],
                tokenSeparators: [",", " "]
            }
        });

        var plan = [];
        $.each({
            "GL": "Gold",
            "FR": "Free"
        }, function (k, v) {
            plan.push({
                id: k,
                text: v
            });
        });

        $('#plans').editable({
            inputclass: 'form-control input-medium',
            source: plan
        });	

        var city = [];
        $.each({
            "GL": "Gold",
            "FR": "Free"
        }, function (k, v) {
            city.push({
                id: k,
                text: v
            });
        });

        $('#city').editable({
            inputclass: 'form-control input-medium',
            source: city
        });			
		
		
        var demoas = [];
        $.each({
            "HO": "Mumbai",
            "AM": "Ahmedabad",
            "SU": "South",
            "WS": "West",
            "CH": "Chennai",
			"HY": "Hyderabad",
			"NR": "North"
        }, function (k, v) {
            demoas.push({
                id: k,
                text: v
            });
        });

        $('#demoas').editable({
            inputclass: 'form-control input-medium',
            source: demoas
        });
		
        var lstatus = [];
        $.each({
            "NC": "Not Contacted",
			"CF": "Contact Failed",
            "DS": "Demo Scheduled",
            "DR": "Demo ReScheduled",
            "NI": "Not Interested",
            "OL": "Opportunity Lost",
			"CL": "Closed"
        }, function (k, v) {
            lstatus.push({
                id: k,
                text: v
            });
        });

        $('#lstatus').editable({
            inputclass: 'form-control input-medium',
            source: lstatus
        });	
			

        $('#address').editable({
            url: '/post',
            value: {
                city: "14th Km, Bijapur-Solapur Highway (NH-13) Kannal Post, Tq & Dist: Bijapur 586 119"
            },
            validate: function (value) {
                if (value.city == '') return 'city is required!';
            },
            display: function (value) {
                if (!value) {
                    $(this).empty();
                    return;
                }
                var html = $('<div>').text(value.city).html();
                $(this).html(html);
            }
        });
    }

    return {
        //main function to initiate the module
        init: function () {

            // inii ajax simulation
            initAjaxMock();

            // init editable elements
            initEditables();
            
            // init editable toggler
            $('#cmp_btn').click(function () {
				var btn = $(this),
					icon = btn.find('.fa'),
					text = btn.find('.text'),
					toggleClass = 'pushme';

				if (btn.hasClass(toggleClass)) {
					icon.removeClass('fa-edit').addClass('fa-save');
					text.text('Save');
				} else {
					icon.removeClass('fa-save').addClass('fa-edit');
					text.text('Edit');
				}
				btn.toggleClass(toggleClass);				
                $('#user .editable').editable('toggleDisabled');
            });		

  /*          // init 
            $('#inline').on('change', function (e) {
                if ($(this).is(':checked')) {
                    window.location.href = 'form_editable.html?mode=inline';
                } else {
                    window.location.href = 'form_editable.html';
                }
            });
*/
            // handle editable elements on hidden event fired
            $('#user .editable, #lead .editable').on('hidden', function (e, reason) {
                if (reason === 'save' || reason === 'nochange') {
                    var $next = $(this).closest('tr').next().find('.editable');
                    if ($('#autoopen').is(':checked')) {
                        setTimeout(function () {
                            $next.editable('show');
                        }, 300);
                    } else {
                        $next.focus();
                    }
                }
            });


        }

    };

}();