/**
Address editable input.
Internally value stored as {date: "Moscow", reason: "Lenina", building: "15"}

@class address
@extends abstractinput
@final
@example
<a href="#" id="address" data-type="address" data-pk="1">awesome</a>
<script>
$(function(){
    $('#address').editable({
        url: '/post',
        title: 'Enter date, reason and building #',
        value: {
            date: "Moscow", 
            reason: "Lenina", 
            building: "15"
        }
    });
});
</script>
**/
(function ($) {
    "use strict";
    
    var Address = function (options) {
        this.init('address', options, Address.defaults);
    };

    //inherit from Abstract input
    $.fn.editableutils.inherit(Address, $.fn.editabletypes.abstractinput);

    $.extend(Address.prototype, {
		
		
		
        /**
        Renders input from tpl

        @method render() 
        **/        
        render: function() {
           this.$input = this.$tpl.find('input');
		   this.$textarea = this.$tpl.find('textarea');
		 
         if (jQuery().datepicker) {
            $('.date-picker').datepicker({
				format: 'dd M yyyy',
				startDate: '-0d',
				todayHighlight:'TRUE',
                rtl: App.isRTL(),
                autoclose: true
            });
        }			 
        },
        
        /**
        Default method to show value in element. Can be overwritten by display option.
        
        @method value2html(value, element) 
        **/
        value2html: function(value, element) {
            if(!value) {
                $(element).empty();
                return; 
            }
            var html = $('<div>').text(value.date).html() ;
            $(element).html(html); 
        },
        
        /**
        Gets value from element's html
        
        @method html2value(html) 
        **/        
        html2value: function(html) {        
          /*
            you may write parsing method to get value by element's html
            e.g. "Moscow, st. Lenina, bld. 15" => {date: "Moscow", reason: "Lenina", building: "15"}
            but for complex structures it's not recommended.
            Better set value directly via javascript, e.g. 
            editable({
                value: {
                    date: "Moscow", 
                    reason: "Lenina", 
                    building: "15"
                }
            });
          */ 
          return null;  
        },
      
       /**
        Converts value to string. 
        It is used in internal comparing (not for sending to server).
        
        @method value2str(value)  
       **/
       value2str: function(value) {
           var str = '';
           if(value) {
               for(var k in value) {
                   str = str + k + ':' + value[k] + ';';  
               }
           }
           return str;
       }, 
       
       /*
        Converts string to value. Used for reading value from 'data-value' attribute.
        
        @method str2value(str)  
       */
       str2value: function(str) {
           /*
           this is mainly for parsing value defined in data-value attribute. 
           If you will always set value by javascript, no need to overwrite it
           */
           return str;
       },                
       
       /**
        Sets value of input.
        
        @method value2input(value) 
        @param {mixed} value
       **/         
       value2input: function(value) {
           if(!value) {
			return;
           }
           this.$input.filter('[name="date"]').val(value.date);
           this.$textarea.filter('[name="reason"]').val(value.reason);
 
       },       
       
       /**
        Returns value of input.
        
        @method input2value() 
       **/          
       input2value: function() { 
           return {
              date: this.$input.filter('[name="date"]').val(), 
              reason: this.$textarea.filter('[name="reason"]').val()
           };

       },        
       
        /**
        Activates input: sets focus on the first field.
        
        @method activate() 
       **/        
       activate: function() {
			this.$textarea.filter('[name="reason"]').focus();
       },  
       
       /**
        Attaches handler to submit form in case of 'showbuttons=false' mode
        
        @method autosubmit() 
       **/       
       autosubmit: function() {
           this.$input.keydown(function (e) {
                if (e.which === 13) {
                    $(this).closest('form').submit();
                }
           });
       }       
    });

    Address.defaults = $.extend({}, $.fn.editabletypes.abstractinput.defaults, {
        tpl: '<form class="form-horizontal" role="form"><div class="form-body"><div class="form-group editable-address" style="margin-bottom:5px;"><label class="col-md-3 control-label">Date</label><div class="col-md-9"><input type="text" name="date" class="form-control form-control-inline date-picker" placeholder="DD MMM YYYY"style="margin: 0px; width: 198px;" data-date-format="dd M yyyy"></input></div></div>'+
             '<div class="form-group editable-address"><label class="col-md-3 control-label">Reason</label><div class="col-md-9"><textarea rows="2" name="reason" class="form-control" style="margin: 0px; width: 198px; height: 55px;resize: none;"></textarea></div></div></div></form>',
             
        inputclass: ''
    });

    $.fn.editabletypes.address = Address;

}(window.jQuery));