/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//function geocodeLatLng() { 
//    var geocoder = new google.maps.Geocoder;
//    var lat = 18.4517672;
//    var long = 73.8303274;
//    var latlng = {lat: parseFloat(lat), lng: parseFloat(long)};
//    geocoder.geocode({'location': latlng}, function (results, status) {
//        if (status === 'OK') {
//            if (results[0]) {
//                console.log("address is : ... "+results[0]);
//                alert(results[0]);
////                $('#reverce_geocoding').html(results[0].formatted_address);
//            } else { 
//                window.alert('No results found');
//            }                     
//        } else {
//            window.alert('Geocoder failed due to: ' + status);
//        }                
//    });             
//}
//geocodeLatLng() ;
function asynchTrail1()
{
    return new Promise(
            function (resolve, reject) {

//                    setTimeout(
//                            function ()
//                            {
                                const error = false;
                                setTimeout(function()
                                {
                                 if($('#trailDiv').html(TimeLineHeader("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")))
                                 {
                                     alert(true);
                                     resolve();
                                 };
                                 
                             },3000)
                                
//                                if(!error)
//                                {
//                                    resolve();
//                                }else
//                                {
//                                    reject('Error has occured');
//                                }
//                            }
//                    , )
//                setTimeout(function () {
//                    $('#trailDiv').html(TimeLineHeader("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//                }, 3000);
            });



}

function asynchTrail(lat, lng, username, lastKnownLocationTimeOnly, visitCounter, TotalVisitDuration, TotalDistanceTravelled, TotalTravelledDurationBetVisits, battery_Percentage, app_version, device_Name, oS_Version, network_type, isGPS, officeDuration, productivity, punchInTime, punch_out_time, punch_out_flag)
{
    return new Promise(
            function (resolve, reject) {


                            const error = false;
//                                 $('#trailDiv').html(TimeLineHeader("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//alert(lat +"  ** "+lng);
                            //GeoCode start
                            geocoder = new google.maps.Geocoder();
                            var latlng = new google.maps.LatLng(lat, lng);
                            geocoder.geocode({'latLng': latlng}, function (results, status) {
//        alert("1 "+status);
                                if (status == google.maps.GeocoderStatus.OK) {
//      alert(results[1])
                                    if (results[1]) {
                                        //formatted address
//         alert(results[0].formatted_address)
                                        //find country name
                                        for (var i = 0; i < results[0].address_components.length; i++) {
                                            for (var b = 0; b < results[0].address_components[i].types.length; b++) {

                                                //there are different types that might hold a city admin_area_lvl_1 usually does in come cases looking for sublocality type will be more appropriate
                                                if (results[0].address_components[i].types[b] == "administrative_area_level_2") {
                                                    //this is the object you are looking for
                                                    city = results[0].address_components[i];
                                                    break;
                                                }
                                            }
                                        }
//                                        city data
//        alert("city.short_name :- "+city.short_name)
//        return city.short_name;

//      
//            $('#trailDiv').html(TimeLineHeader(username," -- ",lastKnownLocationTimeOnly,visitCounter,TotalVisitDuration,TotalDistanceTravelled,convertMintoHHMM(TotalTravelledDurationBetVisits),battery_Percentage,app_version,device_Name,oS_Version,network_type,isGPS,officeDuration,productivity,punchInTime,punch_out_time,punch_out_flag));

                                      if (  $('#trailDiv').html(TimeLineHeader(username, city.short_name, lastKnownLocationTimeOnly, visitCounter, TotalVisitDuration, TotalDistanceTravelled, convertMintoHHMM(TotalTravelledDurationBetVisits), battery_Percentage, app_version, device_Name, oS_Version, network_type, isGPS, officeDuration, productivity, punchInTime, punch_out_time, punch_out_flag)))
                                      {
                                         resolve();  
                                      };

                                    } else {
//          alert("No results found");
                                    }
                                } else {
//        alert("Geocoder failed due to: " + status);
                                }
                            });

            });



}

function initialize1() {
    geocoder = new google.maps.Geocoder();
}

var mobileERRFlag = false;
function callbackTrail(productivity)
{
    //nikhil new
//    alert("callbackTrail");
    if (!isNaN(productivity.toFixed(0)))
    {
//        alert("pro");
//       alert("data_parcent" +   productivity.toFixed(0));
        $("#PreviewGaugeMeter_2").data("percent", Number(productivity.toFixed(0)));
//        $("#PreviewGaugeMeter_2").empty();
        $("#PreviewGaugeMeter_2").gaugeMeter();
//     $("#PreviewGaugeMeter_2").data-percent(productivity.toFixed(0)); 
    } else
    {
//     alert("NAN pro");
        $("#PreviewGaugeMeter_2").data("percent", 0);
//        $("#PreviewGaugeMeter_2").empty();
        $("#PreviewGaugeMeter_2").gaugeMeter();
    }
    if( mobileERRFlag)
    {
//        alert("error in mobile not");
        $("#id_mobileERR").addClass("phone_dtl_err");
    }else
    {
         $("#id_mobileERR").addClass("phone_dtl");
    }
    mobileERRFlag = false;
}

async function codeLatLng1(lat, lng, username, lastKnownLocationTimeOnly, visitCounter, TotalVisitDuration, TotalDistanceTravelled, TotalTravelledDurationBetVisits, battery_Percentage, app_version, device_Name, oS_Version, network_type, isGPS, officeDuration, productivity, punchInTime, punch_out_time, punch_out_flag)
{
if(lat !== "--" && lng !== "--")
{
//    alert( "lat lng !-- !--");
    await asynchTrail(lat, lng, username, lastKnownLocationTimeOnly, visitCounter, TotalVisitDuration, TotalDistanceTravelled, TotalTravelledDurationBetVisits, battery_Percentage, app_version, device_Name, oS_Version, network_type, isGPS, officeDuration, productivity, punchInTime, punch_out_time, punch_out_flag);
//    $('#trailDiv').html(TimeLineHeader(username,"",lastKnownLocationTimeOnly,visitCounter,TotalVisitDuration,TotalDistanceTravelled,convertMintoHHMM(TotalTravelledDurationBetVisits),battery_Percentage,app_version,device_Name,oS_Version,network_type,isGPS,officeDuration,productivity,punchInTime,punch_out_time,punch_out_flag));
    callbackTrail(productivity);
    }else
    {
                   $('#trailDiv').html(TimeLineHeader(username,"--","--",visitCounter,TotalVisitDuration,TotalDistanceTravelled,convertMintoHHMM(TotalTravelledDurationBetVisits),battery_Percentage,app_version,device_Name,oS_Version,network_type,isGPS,officeDuration,productivity,punchInTime,punch_out_time,punch_out_flag));
                     callbackTrail(productivity);
    }
}

var timeLineTemplate = '';
function TimeLineHeader(username, city, lastKnownLocationTimeOnly, visitCounter, TotalVisitDuration, TotalDistanceTravelled, TotalTravelledDurationBetVisits, battery_Percentage, app_version, device_Name, oS_Version, network_type, isGPS, officeDuration, productivity, punchInTime, punch_out_time, punch_out_flag)
{
//    alert("TimeLineHeader");
//    alert("productivity "+productivity);
//    alert("punchInTime "+punchInTime +" punch_out_time "+punch_out_time + "punch_out_flag" +punch_out_flag +" username "+username +" city "+city+" lastKnownLocationTimeOnly "+lastKnownLocationTimeOnly +" visitCounter "+visitCounter+" TotalVisitDuration "+TotalVisitDuration +" TotalDistanceTravelled "+TotalDistanceTravelled +" TotalTravelledDurationBetVisits "+TotalTravelledDurationBetVisits +
//            " battery_Percentage "+battery_Percentage +" app_version "+app_version+" device_Name "+device_Name +" oS_Version "+oS_Version +" network_type "+network_type +" isGPS "+isGPS+" officeDuration "+officeDuration+" productivity "+productivity+" punchInTime "+punchInTime +" punch_out_time "+punch_out_time+"punch_out_flag"+punch_out_flag);
    var TimeLineHeaderTemplate = '';
    TimeLineHeaderTemplate += '<div class="productivity"> ';
    TimeLineHeaderTemplate += '<div class="portlet-title">';

    TimeLineHeaderTemplate += '<div class="pic_block">';
    TimeLineHeaderTemplate += '<div class="avatar">';
//TimeLineHeaderTemplate += '<img alt="" src="assets_TimeLine/img/avatar1_small.jpg">';
//alert(imageURLPath + " "+accountId+" "+loginUserId +" "+time);
    TimeLineHeaderTemplate += '<img alt="" src="' + imageURLPath + accountId + '_' + loginUserId + '_29X29.png?' + time + '" onerror="this.src=\'assets/img/usrsml_29.png\';"/>';
    TimeLineHeaderTemplate += '</div>';
    TimeLineHeaderTemplate += '<div class="info">';
    TimeLineHeaderTemplate += '<div class="caption">' + username + '</div>';
//     alert(typeof city + "IN ***  "+typeof lastKnownLocationTimeOnly);   
    if (punch_out_flag == false)
    {
        TimeLineHeaderTemplate += '<div class="caption2">Punched-in @ ' + punchInTime + '</div>';
    } else if (punch_out_flag == true)
    {
        TimeLineHeaderTemplate += '<div class="caption2">Punched-out @ ' + punch_out_time + '</div>';
    }
//alert(typeof punchInTime + "1***  "+typeof   punch_out_time);
    if ( punchInTime === "undefined" &&  punch_out_time === "undefined")
    {
//        alert(punchInTime + "IN ***  "+punch_out_time);   
        TimeLineHeaderTemplate += '<div class="location"><img alt="" src="assets_TimeLine/img/loc.png">' + ' -- ' + ' @ --</div>';
        
    } else  if ( city == '--' &&  lastKnownLocationTimeOnly == '--')
    {
//        alert(city + "IN ***  "+lastKnownLocationTimeOnly);   
        TimeLineHeaderTemplate += '';
    }else
    {
//        alert("3rd alert");
        TimeLineHeaderTemplate += '<div class="location"><img alt="" src="assets_TimeLine/img/loc.png">' + city + ' @' + lastKnownLocationTimeOnly + '</div>';
    }
    TimeLineHeaderTemplate += '</div>';
    TimeLineHeaderTemplate += '</div>';
    TimeLineHeaderTemplate += '<div class="phone_details">';
    TimeLineHeaderTemplate += '<ul class="nav navbar-nav pull-right">';
    TimeLineHeaderTemplate += '<li class="dropdown" id="header_notification_bar">';
    TimeLineHeaderTemplate += '<a href="#"  class="dropdown-toggle" data-toggle="dropdown" data-click="dropdown" data-close-others="true">';
    TimeLineHeaderTemplate += '<div class="" id = "id_mobileERR"></div>';
    TimeLineHeaderTemplate += '</a>';
    TimeLineHeaderTemplate += '<ul class="dropdown-menu extended notification">';
    TimeLineHeaderTemplate += '<li>';
    TimeLineHeaderTemplate += '<p>';
    TimeLineHeaderTemplate += 'Phone Details';
    TimeLineHeaderTemplate += '</p>';
    TimeLineHeaderTemplate += '</li>';
    TimeLineHeaderTemplate += '<li>';
    TimeLineHeaderTemplate += '<a href="#">';
    TimeLineHeaderTemplate += '<div>';
//TimeLineHeaderTemplate += '<img alt="" src="assets_TimeLine/img/battery10.png">';
    if (typeof battery_Percentage != "undefined")
    {
        TimeLineHeaderTemplate += '<meter style="width: 24px" max="100" low="35" high="55" optimum="100" value=' + battery_Percentage.split("%")[0] + '></meter>'
        if (Number(battery_Percentage.split("%")[0]) < 35)
        {
            mobileERRFlag = true;
//            alert("wromg in battery");
            TimeLineHeaderTemplate += '<span class="time error">';
        } else
        {
            TimeLineHeaderTemplate += '<span class="time">';
        }
    } else
    {
        TimeLineHeaderTemplate += '<meter style="width: 24px" max="100" low="35" high="55" optimum="100" value=""></meter>'
        TimeLineHeaderTemplate += '<span class="time">';
    }

    if (typeof battery_Percentage != "undefined")
    {
        TimeLineHeaderTemplate += '' + battery_Percentage + '';
    } else
    {
        TimeLineHeaderTemplate += '---';
    }
    TimeLineHeaderTemplate += '</span>';
    TimeLineHeaderTemplate += '</div>';
    TimeLineHeaderTemplate += '</a>';
    TimeLineHeaderTemplate += '</li>';
    TimeLineHeaderTemplate += '<li>';
    if (typeof isGPS != "undefined")
    {
        if (isGPS == 'N') {
             mobileERRFlag = true;
//                alert("wromg in gps");
            TimeLineHeaderTemplate += '<a href="#">';
            TimeLineHeaderTemplate += '<img alt="" src="assets_TimeLine/img/gps-off.png">';
            TimeLineHeaderTemplate += '<span class="time error"> GPS OFF';
            TimeLineHeaderTemplate += '</span>';
            TimeLineHeaderTemplate += '</a>';
        } else if (isGPS == 'Y')
            ;
        {
            TimeLineHeaderTemplate += '<a href="#">';
            TimeLineHeaderTemplate += '<img alt="" src="assets_TimeLine/img/gps.png">';
            TimeLineHeaderTemplate += '<span class="time"> GPS ON';
            TimeLineHeaderTemplate += '</span>';
            TimeLineHeaderTemplate += '</a>';
        }
    } else
    {
        TimeLineHeaderTemplate += '<a href="#">';
        TimeLineHeaderTemplate += '<img alt="" src="assets_TimeLine/img/gps.png">';
        TimeLineHeaderTemplate += '<span class="time"> GPS --';
        TimeLineHeaderTemplate += '</span>';
        TimeLineHeaderTemplate += '</a>';
    }
    TimeLineHeaderTemplate += '</li>';
    TimeLineHeaderTemplate += '<li>';
    TimeLineHeaderTemplate += '<a href="#">';
    TimeLineHeaderTemplate += '<img alt="" src="assets_TimeLine/img/network.png">';
    TimeLineHeaderTemplate += '<span class="time">';
    if (typeof network_type != "undefined")
    {
        TimeLineHeaderTemplate += '' + network_type + '';
    } else
    {
        TimeLineHeaderTemplate += '--';
    }
    TimeLineHeaderTemplate += '</span>';
    TimeLineHeaderTemplate += '</a>';
    TimeLineHeaderTemplate += '</li>';
    TimeLineHeaderTemplate += '<li>';
    TimeLineHeaderTemplate += '<a href="#">';
    TimeLineHeaderTemplate += '<img alt="" src="assets_TimeLine/img/fs-pd.png">';
    TimeLineHeaderTemplate += '<span class="time">';
    if (typeof app_version != "undefined")
    {
        TimeLineHeaderTemplate += 'v' + app_version + '';
    } else
    {
        TimeLineHeaderTemplate += 'v--';
    }
    TimeLineHeaderTemplate += '</span>';
    TimeLineHeaderTemplate += '</a>';
    TimeLineHeaderTemplate += '</li>';
    TimeLineHeaderTemplate += '<li>';
    TimeLineHeaderTemplate += '<a href="#">';
    TimeLineHeaderTemplate += '<img alt="" src="assets_TimeLine/img/android.png">';
    TimeLineHeaderTemplate += '<span class="time">';
    if (typeof oS_Version != "undefined")
    {
        TimeLineHeaderTemplate += 'v' + oS_Version + '';
    } else
    {
        TimeLineHeaderTemplate += 'v--';
    }
    TimeLineHeaderTemplate += '</span>';
    TimeLineHeaderTemplate += '</a>';
    TimeLineHeaderTemplate += '</li>';
    TimeLineHeaderTemplate += '<li>';
    TimeLineHeaderTemplate += '<a href="#">';
    TimeLineHeaderTemplate += '<img alt="" src="assets_TimeLine/img/device.png">';
    TimeLineHeaderTemplate += '<span class="time">';
    if (typeof device_Name != "undefined")
    {
        TimeLineHeaderTemplate += '' + device_Name + '';
    } else
    {
        TimeLineHeaderTemplate += '--';
    }
    TimeLineHeaderTemplate += '</span>';
    TimeLineHeaderTemplate += '</a>';
    TimeLineHeaderTemplate += '</li>';
    TimeLineHeaderTemplate += '</ul>';
    TimeLineHeaderTemplate += '</li>	';

    TimeLineHeaderTemplate += '</ul>';
    TimeLineHeaderTemplate += '</div>';

    TimeLineHeaderTemplate += '</div>';

    TimeLineHeaderTemplate += '<div class="prod-title">';
    TimeLineHeaderTemplate += '<div class="prod_block">';

    TimeLineHeaderTemplate += '<div class="prod">';

//TimeLineHeaderTemplate += ' <div class="arc"><span class="count">'+productivity.toFixed(2)+" %"+'</span></div><span>Productivity</span>';
//alert("productivity "+productivity.toFixed(0));
    TimeLineHeaderTemplate += ' <div class="arc"> <div class="GaugeMeter" id="PreviewGaugeMeter_2" data-percent="" data-append=" %" data-size="58" data-theme="White" data-back="RGBa(0,0,0,.2)" data-animate_gauge_colors=true data-animate_text_colors=true data-width="2" data-label="" data-label_color="#FFF"></div>	 </div><span>Productivity</span>';
    TimeLineHeaderTemplate += '</div>';

    TimeLineHeaderTemplate += '<div class="prod2">';
    TimeLineHeaderTemplate += '<div class="arc2"><img alt="" src="assets_TimeLine/img/visit.png"></div><span class="count">' + visitCounter + '</span> <span>Visits</span>';
    TimeLineHeaderTemplate += ' <div class="arc_time">' + TotalVisitDuration + '</div>';
    TimeLineHeaderTemplate += '</div>';

    TimeLineHeaderTemplate += '<div class="prod2">';
    TimeLineHeaderTemplate += '<div class="arc2"><img alt="" src="assets_TimeLine/img/kms.png"></div><span class="count">' + TotalDistanceTravelled + '</span> <span>kms</span>';
    TimeLineHeaderTemplate += '<div class="arc_time">' + TotalTravelledDurationBetVisits + '</div>';
    TimeLineHeaderTemplate += '</div>';

    TimeLineHeaderTemplate += '<div class="prod2">';
    TimeLineHeaderTemplate += '<div class="arc2"><img alt="" src="assets_TimeLine/img/office.png"></div><span class="count"></span> <span>Office</span>';
    TimeLineHeaderTemplate += '<div class="arc_time">' + officeDuration + '</div>';
    TimeLineHeaderTemplate += '</div>';
    TimeLineHeaderTemplate += '</div>';

    TimeLineHeaderTemplate += '</div>'; //doubtfull

//TimeLineHeaderTemplate += '<div class="msg-box">';
//TimeLineHeaderTemplate += '<form class="form-horizontal" role="form" onsubmit="return false">';
//TimeLineHeaderTemplate += '<div class="form-group">';
//TimeLineHeaderTemplate += '<div class="col-md-8">';
//TimeLineHeaderTemplate += '<div class="input-icon">';
//TimeLineHeaderTemplate += '<i class="fa fa-comment fa-flip-horizontal"></i>';
//TimeLineHeaderTemplate += '<input type="search" class="form-control" placeholder="Type Message" style="width:263px;">';
//TimeLineHeaderTemplate += '</div>';
//TimeLineHeaderTemplate += '</div>';
//TimeLineHeaderTemplate += '<div class="col-md-4"><button type="button" class="btn btn-sm btn-info" style="padding: 7px 12px;;float:right;" onclick="messageSendTemplate(\'70\');return false;"> SEND</button></div>';
//TimeLineHeaderTemplate += '</div>';
//TimeLineHeaderTemplate += '</form>		';
//TimeLineHeaderTemplate += '</div>';

    TimeLineHeaderTemplate += '</div>';
//          $(".GaugeMeter").gaugeMeter();
//           $("#GaugeMeter_103").gaugeMeter({percent:15});
    return TimeLineHeaderTemplate;
}

function punchIn(punchInTime, location)
{
    var timeLineTemplate2 = '';
    timeLineTemplate2 += '<div class="container_time right punchin">';
    timeLineTemplate2 += '<div class="content_time">';
    timeLineTemplate2 += '<div class="cnt_dtl">';
    timeLineTemplate2 += '<h5>PUNCH-IN <span>' + punchInTime + '</span></h5>';
    timeLineTemplate2 += '<p><a data-toggle="modal" href="#responsive">' + location + '</a></p>';
    timeLineTemplate2 += '</div>';
    timeLineTemplate2 += '</div>';
    timeLineTemplate2 += '</div>';
    return timeLineTemplate2;
}
function punchOut(punchOutTime, location)
{
    var timeLineTemplate2 = '';
    timeLineTemplate2 += '<div class="container_time right punchout">';
    timeLineTemplate2 += '<div class="content_time_end">';
    timeLineTemplate2 += '<div class="cnt_dtl">';
    timeLineTemplate2 += '<h5>PUNCH-OUT <span>' + punchOutTime + '</span></h5>';
    timeLineTemplate2 += '<p><a data-toggle="modal" href="#responsive">' + location + '</a></p>';
    timeLineTemplate2 += '</div>';
    timeLineTemplate2 += '</div>';
    timeLineTemplate2 += '</div>';
    return timeLineTemplate2;
}
function lastknownLocationFun(punchOutTime, location)
{
    var lastknownLocationFun = '';
    lastknownLocationFun += '<div class="container_time right punchin">';
    lastknownLocationFun += '<div class="content_time_end">';
    lastknownLocationFun += '<div class="cnt_dtl">';
    lastknownLocationFun += '<h5>LAST KNOWN LOCATION <span>' + punchOutTime + '</span></h5>';
    lastknownLocationFun += '<p><a data-toggle="modal" href="#responsive">' + location + '</a></p>';
    lastknownLocationFun += '</div>';
    lastknownLocationFun += '</div>';
    lastknownLocationFun += '</div>';
    return lastknownLocationFun;
}

function travelArrow(DistanceTravelled, TravelStart, TravelEnd, TravelDuration, punchInTime, FirstTravelDuration)
{
//console.log("travelArrow TravelStart "+TravelStart);

    var travelArrowTemplate = '';
    travelArrowTemplate += '<div class="container_travel travel_arrow">';
    travelArrowTemplate += '<div class="travel_time">';
    if (typeof TravelStart == "undefined")
    {
//    console.log("undefined");
        console.log("travelArrow punchInTime " + punchInTime);
        console.log("travelArrow TravelEnd " + TravelEnd);
        console.log("travelArrow TimeDiff " + TimeDiff(punchInTime, TravelEnd));
        console.log("travelArrow FirstTravelDuration " + FirstTravelDuration);
        travelArrowTemplate += '<h5>Travel (' + DistanceTravelled + 'km) <span>' + punchInTime + '  -  ' + TravelEnd + '<br/>(' + convertMintoHHMM(TravelDuration) + ')</span></h5>';
//        travelArrowTemplate += '<h5>Travel ('+DistanceTravelled+') <span>'+punchInTime+'  -  '+TravelEnd+'</span></h5>';
//travelArrowTemplate += '<h5>Travel ('+DistanceTravelled+' km)</h5>';
    } else
    {
        travelArrowTemplate += '<h5>Travel (' + DistanceTravelled + ' km) <span>' + TravelStart + '  -  ' + TravelEnd + '<br/>(' + convertMintoHHMM(TravelDuration) + ')</span></h5>';
//travelArrowTemplate += '<h5>Travel ('+DistanceTravelled+' km) <span>'+TravelStart+'  -  '+TravelEnd+'</span></h5>';
//travelArrowTemplate += '<h5>Travel ('+DistanceTravelled+' km)</h5>';
    }
    travelArrowTemplate += '</div>';
    travelArrowTemplate += '</div>';
    return travelArrowTemplate;
}

function OfficeLocation(TimeStart, TimeEnd, Duration, location)
{
//office strt
    var OfficeLocationTemplate = '';
    OfficeLocationTemplate += '<div class="container_time right work">';
    OfficeLocationTemplate += '<div class="content_time">';
    OfficeLocationTemplate += '<div class="cnt_dtl">';
    OfficeLocationTemplate += '<h5>AT WORK <span>' + TimeStart + '  -  ' + TimeEnd + '<br/>(' + Duration + ')</span></h5>';
    OfficeLocationTemplate += '<p><a href="#">Office</a></p>';
    OfficeLocationTemplate += '</div>';
    OfficeLocationTemplate += '</div>';
    OfficeLocationTemplate += '</div>';
    return OfficeLocationTemplate;
}
//var visitCount = 1 ;
function plannedVisit(TimeStart, TimeEnd, Duration, customerName, titleVisit, descriptionVisit) {
//    alert(" customerName "+customerName+" titleVisit "+titleVisit +" descriptionVisit "+descriptionVisit);
    var plannedVisitTemplate = '';
    plannedVisitTemplate += '<div class="container_time right visit_comp">';
    plannedVisitTemplate += '<div class="content_time">';

    plannedVisitTemplate += '<div class="cnt_dtl">';
    plannedVisitTemplate += '<h5> PLANNED VISIT <span>' + TimeStart + '  -  ' + TimeEnd + '<br/>(' + Duration + ')</span></h5>';
    plannedVisitTemplate += '<p><a href="#">' + customerName + '</a></p>';
    plannedVisitTemplate += '<div class="titl">' + titleVisit + '</div>';
//alert("descriptionVisit "+descriptionVisit);
    if (descriptionVisit !== "") {
        // obj is a valid variable, do something here. 
        plannedVisitTemplate += '<div class="dscp">Description1: ' + descriptionVisit + '</div>';
    } else {
//    alert("got blank");
    }

    plannedVisitTemplate += '</div>';


//plannedVisitTemplate += '<div class="exp">Expenses: INR 250 ';
//plannedVisitTemplate += '<a href="#" data-toggle="collapse" data-target="#demo" class="list tooltips" data-placement="bottom" data-original-title="Expense List"><i class="fa fa-bars"></i></a>';
//plannedVisitTemplate += '<a data-toggle="modal" href="#expenses" class="add tooltips" data-placement="bottom" data-original-title="Add Expense"><i class="fa fa-plus-circle fa-lg" aria-hidden="true"></i></a>';
//plannedVisitTemplate += '<div id="demo" class="collapse">';
//plannedVisitTemplate += '<div class="form-body">';
//plannedVisitTemplate += '<div class="row">';
//plannedVisitTemplate += '<div class="col-md-12">';
//
//            //table strt
//plannedVisitTemplate += '<table class="table table-striped table-bordered" id="sample_2">';
//plannedVisitTemplate += '<thead>';
//plannedVisitTemplate += '<tr>';
//plannedVisitTemplate += '<th>';
//plannedVisitTemplate += 'Exp. Cat.';
//plannedVisitTemplate += '</th>';
//plannedVisitTemplate += '<th>';
//plannedVisitTemplate += 'Amt. ( INR )';
//plannedVisitTemplate += '</th>';
//plannedVisitTemplate += '<th>';
//plannedVisitTemplate += '</th>';
//plannedVisitTemplate += '</tr>';
//plannedVisitTemplate += '</thead>';
//plannedVisitTemplate += '<tbody>';
//plannedVisitTemplate += '<tr>';
//plannedVisitTemplate += '<td>';
//plannedVisitTemplate += ' Food';
//plannedVisitTemplate += '</td>';
//plannedVisitTemplate += '<td>';
//plannedVisitTemplate += '100';
//plannedVisitTemplate += '</td>';
//plannedVisitTemplate += '<td>';
//plannedVisitTemplate += '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#all_expenses" data-placement="bottom" data-original-title="Edit"><i class="fa fa-edit"></i></button>';
//plannedVisitTemplate += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" data-original-title="Delete"><i class="fa fa-trash-o"></i></button>';
//plannedVisitTemplate += '</td>		';
//plannedVisitTemplate += '</tr>';
//plannedVisitTemplate += '<tr>';
//plannedVisitTemplate += '<td>';
//plannedVisitTemplate += 'Travel';
//plannedVisitTemplate += '</td>';
//plannedVisitTemplate += '<td>';
//plannedVisitTemplate += '150';
//plannedVisitTemplate += '</td>';
//plannedVisitTemplate += '<td>';
//plannedVisitTemplate += '<button type="button" class="btn btn-default btn-xs tooltips" data-toggle="modal" href="#all_expenses" data-placement="bottom" data-original-title="Edit"><i class="fa fa-edit"></i></button>';
//plannedVisitTemplate += '<button type="button" class="btn btn-default btn-xs tooltips" data-placement="bottom" data-original-title="Delete"><i class="fa fa-trash-o"></i></button>';
//plannedVisitTemplate += '</td>';
//plannedVisitTemplate += '</tr>';
//plannedVisitTemplate += '</tbody>';
//plannedVisitTemplate += '<tfoot>';
//plannedVisitTemplate += '<tr>';
//plannedVisitTemplate += '<th>';
//plannedVisitTemplate += 'Total';
//plannedVisitTemplate += '</th>';
//plannedVisitTemplate += '<th>';
//plannedVisitTemplate += '250';
//plannedVisitTemplate += '</th>';
//plannedVisitTemplate += '<th>';
//plannedVisitTemplate += '</th>';
//plannedVisitTemplate += '</tr>';
//plannedVisitTemplate += '</tfoot>';
//plannedVisitTemplate += '</table>';
//                    //table end
//plannedVisitTemplate += '</div>';
//plannedVisitTemplate += '</div>';
//plannedVisitTemplate += '</div>';
//plannedVisitTemplate += '</div>';
//plannedVisitTemplate += '</div>';

    plannedVisitTemplate += '</div>';
    plannedVisitTemplate += '</div>';
    return plannedVisitTemplate;
}

function UnPlannedVisit(TimeStart, TimeEnd, Duration, location)
{
    var UnPlannedVisitTemplate = '';
    UnPlannedVisitTemplate += '<div class="container_time right unplan_comp">';
    UnPlannedVisitTemplate += '<div class="content_time">';
    UnPlannedVisitTemplate += '<div class="cnt_dtl">';

    UnPlannedVisitTemplate += '<h5>UNPLANNED STOP <span>' + TimeStart + '  -  ' + TimeEnd + '<br/>(' + Duration + ')</span></h5>';


    UnPlannedVisitTemplate += '<div class="dropdown" id="switch-boards">';
//alert("location "+location);
    if (location == '')
    {
        UnPlannedVisitTemplate += '<p><a href="#">Unidentified</a>';
    } else
    {
        UnPlannedVisitTemplate += '<p><a href="#">' + location + '</a>';
    }
//UnPlannedVisitTemplate += '<button type="button" class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" data-target="#switch-boards" style="    margin-left: 7px;"><i class="fa fa-angle-down"></i></button>';

    UnPlannedVisitTemplate += '<ul class="dropdown-menu other" role="menu">';

    UnPlannedVisitTemplate += '<form action="#">';
    UnPlannedVisitTemplate += '<div class="input-icon">';
    UnPlannedVisitTemplate += '<i class="fa fa-map-marker"></i>';
    UnPlannedVisitTemplate += '<input type="search" class="form-control" placeholder="Search for place or Address">';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</form>';

//unplanned visit scroller strt
    UnPlannedVisitTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" style=="max-height: 100px!important;">';
// list strt
    UnPlannedVisitTemplate += '<li>';
    UnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal">';
    UnPlannedVisitTemplate += '<div class="map_block">';
    UnPlannedVisitTemplate += '<div class="map">';
    UnPlannedVisitTemplate += '<i class="fa fa-map-marker"></i>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '<div class="company">';
    UnPlannedVisitTemplate += '<div class="cname">1&1 Broadband Internet</div>';
    UnPlannedVisitTemplate += '<div class="address">Anand Kawach, Runwal Nagar, Thane West, Thane, Maharashtra 400601</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</a>';
    UnPlannedVisitTemplate += '</li>';
//list end

//list strt
    UnPlannedVisitTemplate += '<li>';
    UnPlannedVisitTemplate += '<a data-toggle="modal" data-target="#myModal">';
    UnPlannedVisitTemplate += '<div class="map_block">';
    UnPlannedVisitTemplate += '<div class="map">';
    UnPlannedVisitTemplate += '<i class="fa fa-map-marker"></i>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '<div class="company">';
    UnPlannedVisitTemplate += '<div class="cname">24 Frames Digital</div>';
    UnPlannedVisitTemplate += '<div class="address">511-C, Shatrunjay Darshan, Motishah Cross Lane, Byculla 400027</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</a>';
    UnPlannedVisitTemplate += '</li>';
//list end

//list strt
    UnPlannedVisitTemplate += '<li>';
    UnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal">';
    UnPlannedVisitTemplate += '<div class="map_block">';
    UnPlannedVisitTemplate += '<div class="map">';
    UnPlannedVisitTemplate += '<i class="fa fa-map-marker"></i>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '<div class="company">';
    UnPlannedVisitTemplate += '<div class="cname">QLC Pvt Ltd.</div>';
    UnPlannedVisitTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</a>';
    UnPlannedVisitTemplate += '</li>';
//list end

//list strt
    UnPlannedVisitTemplate += '<li>';
    UnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal">';
    UnPlannedVisitTemplate += '<div class="map_block">';
    UnPlannedVisitTemplate += '<div class="map">';
    UnPlannedVisitTemplate += ' <i class="fa fa-map-marker"></i>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '<div class="company">';
    UnPlannedVisitTemplate += '<div class="cname">QLC Pvt Ltd.</div>';
    UnPlannedVisitTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</a>';
    UnPlannedVisitTemplate += '</li>';
//list end

//list strts
    UnPlannedVisitTemplate += '<li>';
    UnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal">';
    UnPlannedVisitTemplate += '<div class="map_block">';
    UnPlannedVisitTemplate += '<div class="map">';
    UnPlannedVisitTemplate += ' <i class="fa fa-map-marker"></i>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '<div class="company">';
    UnPlannedVisitTemplate += '<div class="cname">QLC Pvt Ltd.</div>';
    UnPlannedVisitTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</a>';
    UnPlannedVisitTemplate += '</li>';
//list end

    UnPlannedVisitTemplate += '</div>';
//unplanned visit scroller end

    UnPlannedVisitTemplate += '<li class="or">OR</li>';

    UnPlannedVisitTemplate += '<li>';
    UnPlannedVisitTemplate += '<a href="#">';
    UnPlannedVisitTemplate += '<div class="map">';
    UnPlannedVisitTemplate += '<i class="fa fa-home"></i>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '<div class="company">';
    UnPlannedVisitTemplate += '<div class="cname">Home</div>';
    UnPlannedVisitTemplate += '<div class="address">Hiranandani Meadows, Phokran Road 2, Thane West</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</a>';
    UnPlannedVisitTemplate += '</li>';

    UnPlannedVisitTemplate += '<li>';
    UnPlannedVisitTemplate += '<a href="#">';
    UnPlannedVisitTemplate += '<div class="map">';
    UnPlannedVisitTemplate += '<i class="fa fa-building"></i>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '<div class="company">';
    UnPlannedVisitTemplate += '<div class="cname">Work</div>';
    UnPlannedVisitTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</li>';

    UnPlannedVisitTemplate += '<li>';
    UnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal2">';
    UnPlannedVisitTemplate += '<div class="map">';
    UnPlannedVisitTemplate += '<i class="fa fa-user"></i>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '<div class="company">';
    UnPlannedVisitTemplate += '<div class="cname">Personal</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</a>';
    UnPlannedVisitTemplate += '</li>';

    UnPlannedVisitTemplate += '</ul>';

    UnPlannedVisitTemplate += '</p>';

    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</div>';
    UnPlannedVisitTemplate += '</div>';
    return UnPlannedVisitTemplate;
//$$$$$$$$$$$$$$$$$$$$$
}

function PunchInUnPlannedVisitTogether(TimeStart, TimeEnd, Duration, location, punchInTime)
{
    var PunchInUnPlannedVisitTemplate = '';
    PunchInUnPlannedVisitTemplate += '<div class="container_time right punchin">';

    PunchInUnPlannedVisitTemplate += '<div class="content_time">';

    PunchInUnPlannedVisitTemplate += '<div class="cnt_dtl">';
    PunchInUnPlannedVisitTemplate += '<div class="">';
//    PunchInUnPlannedVisitTemplate += '<div class="content_time">';
    PunchInUnPlannedVisitTemplate += '<div class="cnt_dtl">';
    PunchInUnPlannedVisitTemplate += '<h5>PUNCH-IN ' + punchInTime + '</h5>';
    PunchInUnPlannedVisitTemplate += '</div>';
//    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</div>';

    PunchInUnPlannedVisitTemplate += '<h5>UNPLANNED STOP <span>' + TimeStart + '  -  ' + TimeEnd + '<br/>(' + Duration + ')</span></h5>';


    PunchInUnPlannedVisitTemplate += '<div class="dropdown" id="switch-boards">';
    if (location == '')
    {
        PunchInUnPlannedVisitTemplate += '<p><a href="#">Unidentified</a>';
    } else
    {
        PunchInUnPlannedVisitTemplate += '<p><a href="#">' + location + '</a>';
    }
//PunchInUnPlannedVisitTemplate += '<button type="button" class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" data-target="#switch-boards" style="    margin-left: 7px;"><i class="fa fa-angle-down"></i></button>';

    PunchInUnPlannedVisitTemplate += '<ul class="dropdown-menu other" role="menu">';

    PunchInUnPlannedVisitTemplate += '<form action="#">';
    PunchInUnPlannedVisitTemplate += '<div class="input-icon">';
    PunchInUnPlannedVisitTemplate += '<i class="fa fa-map-marker"></i>';
    PunchInUnPlannedVisitTemplate += '<input type="search" class="form-control" placeholder="Search for place or Address">';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</form>';

//unplanned visit scroller strt
    PunchInUnPlannedVisitTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" style=="max-height: 100px!important;">';
// list strt
    PunchInUnPlannedVisitTemplate += '<li>';
    PunchInUnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal">';
    PunchInUnPlannedVisitTemplate += '<div class="map_block">';
    PunchInUnPlannedVisitTemplate += '<div class="map">';
    PunchInUnPlannedVisitTemplate += '<i class="fa fa-map-marker"></i>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '<div class="company">';
    PunchInUnPlannedVisitTemplate += '<div class="cname">1&1 Broadband Internet</div>';
    PunchInUnPlannedVisitTemplate += '<div class="address">Anand Kawach, Runwal Nagar, Thane West, Thane, Maharashtra 400601</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</a>';
    PunchInUnPlannedVisitTemplate += '</li>';
//list end

//list strt
    PunchInUnPlannedVisitTemplate += '<li>';
    PunchInUnPlannedVisitTemplate += '<a data-toggle="modal" data-target="#myModal">';
    PunchInUnPlannedVisitTemplate += '<div class="map_block">';
    PunchInUnPlannedVisitTemplate += '<div class="map">';
    PunchInUnPlannedVisitTemplate += '<i class="fa fa-map-marker"></i>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '<div class="company">';
    PunchInUnPlannedVisitTemplate += '<div class="cname">24 Frames Digital</div>';
    PunchInUnPlannedVisitTemplate += '<div class="address">511-C, Shatrunjay Darshan, Motishah Cross Lane, Byculla 400027</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</a>';
    PunchInUnPlannedVisitTemplate += '</li>';
//list end

//list strt
    PunchInUnPlannedVisitTemplate += '<li>';
    PunchInUnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal">';
    PunchInUnPlannedVisitTemplate += '<div class="map_block">';
    PunchInUnPlannedVisitTemplate += '<div class="map">';
    PunchInUnPlannedVisitTemplate += '<i class="fa fa-map-marker"></i>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '<div class="company">';
    PunchInUnPlannedVisitTemplate += '<div class="cname">QLC Pvt Ltd.</div>';
    PunchInUnPlannedVisitTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</a>';
    PunchInUnPlannedVisitTemplate += '</li>';
//list end

//list strt
    PunchInUnPlannedVisitTemplate += '<li>';
    PunchInUnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal">';
    PunchInUnPlannedVisitTemplate += '<div class="map_block">';
    PunchInUnPlannedVisitTemplate += '<div class="map">';
    PunchInUnPlannedVisitTemplate += ' <i class="fa fa-map-marker"></i>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '<div class="company">';
    PunchInUnPlannedVisitTemplate += '<div class="cname">QLC Pvt Ltd.</div>';
    PunchInUnPlannedVisitTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</a>';
    PunchInUnPlannedVisitTemplate += '</li>';
//list end

//list strts
    PunchInUnPlannedVisitTemplate += '<li>';
    PunchInUnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal">';
    PunchInUnPlannedVisitTemplate += '<div class="map_block">';
    PunchInUnPlannedVisitTemplate += '<div class="map">';
    PunchInUnPlannedVisitTemplate += ' <i class="fa fa-map-marker"></i>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '<div class="company">';
    PunchInUnPlannedVisitTemplate += '<div class="cname">QLC Pvt Ltd.</div>';
    PunchInUnPlannedVisitTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</a>';
    PunchInUnPlannedVisitTemplate += '</li>';
//list end

    PunchInUnPlannedVisitTemplate += '</div>';
//unplanned visit scroller end

    PunchInUnPlannedVisitTemplate += '<li class="or">OR</li>';

    PunchInUnPlannedVisitTemplate += '<li>';
    PunchInUnPlannedVisitTemplate += '<a href="#">';
    PunchInUnPlannedVisitTemplate += '<div class="map">';
    PunchInUnPlannedVisitTemplate += '<i class="fa fa-home"></i>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '<div class="company">';
    PunchInUnPlannedVisitTemplate += '<div class="cname">Home</div>';
    PunchInUnPlannedVisitTemplate += '<div class="address">Hiranandani Meadows, Phokran Road 2, Thane West</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</a>';
    PunchInUnPlannedVisitTemplate += '</li>';

    PunchInUnPlannedVisitTemplate += '<li>';
    PunchInUnPlannedVisitTemplate += '<a href="#">';
    PunchInUnPlannedVisitTemplate += '<div class="map">';
    PunchInUnPlannedVisitTemplate += '<i class="fa fa-building"></i>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '<div class="company">';
    PunchInUnPlannedVisitTemplate += '<div class="cname">Work</div>';
    PunchInUnPlannedVisitTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</li>';

    PunchInUnPlannedVisitTemplate += '<li>';
    PunchInUnPlannedVisitTemplate += '<a data-toggle="modal" href="#myModal2">';
    PunchInUnPlannedVisitTemplate += '<div class="map">';
    PunchInUnPlannedVisitTemplate += '<i class="fa fa-user"></i>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '<div class="company">';
    PunchInUnPlannedVisitTemplate += '<div class="cname">Personal</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</a>';
    PunchInUnPlannedVisitTemplate += '</li>';

    PunchInUnPlannedVisitTemplate += '</ul>';
    PunchInUnPlannedVisitTemplate += '</p>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    PunchInUnPlannedVisitTemplate += '</div>';
    return PunchInUnPlannedVisitTemplate;
//$$$$$$$$$$$$$$$$$$$$$
}
function UnPlannedVisitAutoPunchOut(TimeStart, TimeEnd, Duration, location, punchInTime)
{
    var UnPlannedVisitAutoPunchOut = '';
    UnPlannedVisitAutoPunchOut += '<div class="container_time right punchin">';

    UnPlannedVisitAutoPunchOut += '<div class="content_time_end">';



    UnPlannedVisitAutoPunchOut += '<h5>UNPLANNED STOP <span>' + TimeStart + '  -  ' + TimeEnd + '<br/>(' + Duration + ')</span></h5>';


    UnPlannedVisitAutoPunchOut += '<div class="dropdown" id="switch-boards">';
    if (location == '')
    {
        UnPlannedVisitAutoPunchOut += '<p><a href="#">Unidentified</a>';
    } else
    {
        UnPlannedVisitAutoPunchOut += '<p><a href="#">' + location + '</a>';
    }
//UnPlannedVisitAutoPunchOut += '<button type="button" class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" data-target="#switch-boards" style="    margin-left: 7px;"><i class="fa fa-angle-down"></i></button>';

    UnPlannedVisitAutoPunchOut += '<ul class="dropdown-menu other" role="menu">';

    UnPlannedVisitAutoPunchOut += '<form action="#">';
    UnPlannedVisitAutoPunchOut += '<div class="input-icon">';
    UnPlannedVisitAutoPunchOut += '<i class="fa fa-map-marker"></i>';
    UnPlannedVisitAutoPunchOut += '<input type="search" class="form-control" placeholder="Search for place or Address">';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</form>';

//unplanned visit scroller strt
    UnPlannedVisitAutoPunchOut += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" style=="max-height: 100px!important;">';
// list strt
    UnPlannedVisitAutoPunchOut += '<li>';
    UnPlannedVisitAutoPunchOut += '<a data-toggle="modal" href="#myModal">';
    UnPlannedVisitAutoPunchOut += '<div class="map_block">';
    UnPlannedVisitAutoPunchOut += '<div class="map">';
    UnPlannedVisitAutoPunchOut += '<i class="fa fa-map-marker"></i>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '<div class="company">';
    UnPlannedVisitAutoPunchOut += '<div class="cname">1&1 Broadband Internet</div>';
    UnPlannedVisitAutoPunchOut += '<div class="address">Anand Kawach, Runwal Nagar, Thane West, Thane, Maharashtra 400601</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</a>';
    UnPlannedVisitAutoPunchOut += '</li>';
//list end

//list strt
    UnPlannedVisitAutoPunchOut += '<li>';
    UnPlannedVisitAutoPunchOut += '<a data-toggle="modal" data-target="#myModal">';
    UnPlannedVisitAutoPunchOut += '<div class="map_block">';
    UnPlannedVisitAutoPunchOut += '<div class="map">';
    UnPlannedVisitAutoPunchOut += '<i class="fa fa-map-marker"></i>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '<div class="company">';
    UnPlannedVisitAutoPunchOut += '<div class="cname">24 Frames Digital</div>';
    UnPlannedVisitAutoPunchOut += '<div class="address">511-C, Shatrunjay Darshan, Motishah Cross Lane, Byculla 400027</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</a>';
    UnPlannedVisitAutoPunchOut += '</li>';
//list end

//list strt
    UnPlannedVisitAutoPunchOut += '<li>';
    UnPlannedVisitAutoPunchOut += '<a data-toggle="modal" href="#myModal">';
    UnPlannedVisitAutoPunchOut += '<div class="map_block">';
    UnPlannedVisitAutoPunchOut += '<div class="map">';
    UnPlannedVisitAutoPunchOut += '<i class="fa fa-map-marker"></i>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '<div class="company">';
    UnPlannedVisitAutoPunchOut += '<div class="cname">QLC Pvt Ltd.</div>';
    UnPlannedVisitAutoPunchOut += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</a>';
    UnPlannedVisitAutoPunchOut += '</li>';
//list end

//list strt
    UnPlannedVisitAutoPunchOut += '<li>';
    UnPlannedVisitAutoPunchOut += '<a data-toggle="modal" href="#myModal">';
    UnPlannedVisitAutoPunchOut += '<div class="map_block">';
    UnPlannedVisitAutoPunchOut += '<div class="map">';
    UnPlannedVisitAutoPunchOut += ' <i class="fa fa-map-marker"></i>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '<div class="company">';
    UnPlannedVisitAutoPunchOut += '<div class="cname">QLC Pvt Ltd.</div>';
    UnPlannedVisitAutoPunchOut += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</a>';
    UnPlannedVisitAutoPunchOut += '</li>';
//list end

//list strts
    UnPlannedVisitAutoPunchOut += '<li>';
    UnPlannedVisitAutoPunchOut += '<a data-toggle="modal" href="#myModal">';
    UnPlannedVisitAutoPunchOut += '<div class="map_block">';
    UnPlannedVisitAutoPunchOut += '<div class="map">';
    UnPlannedVisitAutoPunchOut += ' <i class="fa fa-map-marker"></i>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '<div class="company">';
    UnPlannedVisitAutoPunchOut += '<div class="cname">QLC Pvt Ltd.</div>';
    UnPlannedVisitAutoPunchOut += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</a>';
    UnPlannedVisitAutoPunchOut += '</li>';
//list end

    UnPlannedVisitAutoPunchOut += '</div>';
//unplanned visit scroller end

    UnPlannedVisitAutoPunchOut += '<li class="or">OR</li>';

    UnPlannedVisitAutoPunchOut += '<li>';
    UnPlannedVisitAutoPunchOut += '<a href="#">';
    UnPlannedVisitAutoPunchOut += '<div class="map">';
    UnPlannedVisitAutoPunchOut += '<i class="fa fa-home"></i>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '<div class="company">';
    UnPlannedVisitAutoPunchOut += '<div class="cname">Home</div>';
    UnPlannedVisitAutoPunchOut += '<div class="address">Hiranandani Meadows, Phokran Road 2, Thane West</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</a>';
    UnPlannedVisitAutoPunchOut += '</li>';

    UnPlannedVisitAutoPunchOut += '<li>';
    UnPlannedVisitAutoPunchOut += '<a href="#">';
    UnPlannedVisitAutoPunchOut += '<div class="map">';
    UnPlannedVisitAutoPunchOut += '<i class="fa fa-building"></i>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '<div class="company">';
    UnPlannedVisitAutoPunchOut += '<div class="cname">Work</div>';
    UnPlannedVisitAutoPunchOut += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</li>';

    UnPlannedVisitAutoPunchOut += '<li>';
    UnPlannedVisitAutoPunchOut += '<a data-toggle="modal" href="#myModal2">';
    UnPlannedVisitAutoPunchOut += '<div class="map">';
    UnPlannedVisitAutoPunchOut += '<i class="fa fa-user"></i>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '<div class="company">';
    UnPlannedVisitAutoPunchOut += '<div class="cname">Personal</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</a>';
    UnPlannedVisitAutoPunchOut += '</li>';

    UnPlannedVisitAutoPunchOut += '</ul>';
    UnPlannedVisitAutoPunchOut += '</p>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    UnPlannedVisitAutoPunchOut += '</div>';
    return UnPlannedVisitAutoPunchOut;
//$$$$$$$$$$$$$$$$$$$$$
}

function pendingVisits(startTime, endTime, VisitDuration, customername, title, description)
{
    var pendingVisitsTemplate = '';
    pendingVisitsTemplate += '<div class="container_time right visit">';
    pendingVisitsTemplate += '<div class="content_time">';
    pendingVisitsTemplate += '<div class="cnt_dtl">';
    pendingVisitsTemplate += '<h5>03 SCHEDULED VISIT <span>' + startTime + '  -  ' + endTime + '<br/>(' + VisitDuration + ')</span></h5>';
    pendingVisitsTemplate += '<div class="dropdown" id="switch-boards">';
    pendingVisitsTemplate += '<p><a href="#">' + customername + '</a>';
//pendingVisitsTemplate += '<button type="button" class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown" data-target="#switch-boards" style="    margin-left: 7px;"><i class="fa fa-angle-down"></i></button>	';
    pendingVisitsTemplate += '<ul class="dropdown-menu other" role="menu">';
    pendingVisitsTemplate += '<form action="#">';
    pendingVisitsTemplate += '<div class="input-icon">';
    pendingVisitsTemplate += '<i class="fa fa-map-marker"></i>';
    pendingVisitsTemplate += '<input type="search" class="form-control" placeholder="Search for place or Address">';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</form>';
    pendingVisitsTemplate += '<div class="scroller" data-always-visible="1" data-rail-visible1="1" style="max-height: 100px!important;">';

    pendingVisitsTemplate += '<li>';
    pendingVisitsTemplate += '<a data-toggle="modal" href="#myModal">';
    pendingVisitsTemplate += '<div class="map_block">';
    pendingVisitsTemplate += '<div class="map">';
    pendingVisitsTemplate += '<i class="fa fa-map-marker"></i>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<div class="company">';
    pendingVisitsTemplate += '<div class="cname">1&1 Broadband Internet</div>';
    pendingVisitsTemplate += '<div class="address">Anand Kawach, Runwal Nagar, Thane West, Thane, Maharashtra 400601</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</a>';
    pendingVisitsTemplate += '</li>';

    pendingVisitsTemplate += '<li>';
    pendingVisitsTemplate += '<a data-toggle="modal" data-target="#myModal">';
    pendingVisitsTemplate += '<div class="map_block">';
    pendingVisitsTemplate += '<div class="map">';
    pendingVisitsTemplate += '<i class="fa fa-map-marker"></i>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<div class="company">';
    pendingVisitsTemplate += '<div class="cname">24 Frames Digital</div>';
    pendingVisitsTemplate += '<div class="address">511-C, Shatrunjay Darshan, Motishah Cross Lane, Byculla 400027</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</a>';
    pendingVisitsTemplate += '</li>';

    pendingVisitsTemplate += '<li>';
    pendingVisitsTemplate += '<a data-toggle="modal" href="#myModal">';
    pendingVisitsTemplate += '<div class="map_block">';
    pendingVisitsTemplate += '<div class="map">';
    pendingVisitsTemplate += ' <i class="fa fa-map-marker"></i>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<div class="company">';
    pendingVisitsTemplate += '<div class="cname">QLC Pvt Ltd.</div>';
    pendingVisitsTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</a>';
    pendingVisitsTemplate += '</li>';

    pendingVisitsTemplate += '<li>';
    pendingVisitsTemplate += '<a data-toggle="modal" href="#myModal">';
    pendingVisitsTemplate += '<div class="map_block">';
    pendingVisitsTemplate += '<div class="map">';
    pendingVisitsTemplate += ' <i class="fa fa-map-marker"></i>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<div class="company">';
    pendingVisitsTemplate += '<div class="cname">QLC Pvt Ltd.</div>';
    pendingVisitsTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</a>';
    pendingVisitsTemplate += '</li>';

    pendingVisitsTemplate += '<li>';
    pendingVisitsTemplate += '<a data-toggle="modal" href="#myModal">';
    pendingVisitsTemplate += '<div class="map_block">';
    pendingVisitsTemplate += '<div class="map">';
    pendingVisitsTemplate += '<i class="fa fa-map-marker"></i>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<div class="company">';
    pendingVisitsTemplate += '<div class="cname">QLC Pvt Ltd.</div>';
    pendingVisitsTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</a>';
    pendingVisitsTemplate += '</li>';

    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<li class="or">OR</li>';

    pendingVisitsTemplate += '<li>';
    pendingVisitsTemplate += '<a href="#">';
    pendingVisitsTemplate += '<div class="map">';
    pendingVisitsTemplate += '<i class="fa fa-home"></i>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<div class="company">';
    pendingVisitsTemplate += '<div class="cname">Home</div>';
    pendingVisitsTemplate += '<div class="address">Hiranandani Meadows, Phokran Road 2, Thane West</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</a>';
    pendingVisitsTemplate += '</li>';

    pendingVisitsTemplate += '<li>';
    pendingVisitsTemplate += '<a href="#">';
    pendingVisitsTemplate += '<div class="map">';
    pendingVisitsTemplate += '<i class="fa fa-building"></i>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<div class="company">';
    pendingVisitsTemplate += '<div class="cname">Work</div>';
    pendingVisitsTemplate += '<div class="address">208, Shreepal Complex, Suren road, Andheri East</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</a>';
    pendingVisitsTemplate += '</li>';

    pendingVisitsTemplate += '<li>';
    pendingVisitsTemplate += '<a href="#">';
    pendingVisitsTemplate += '<div class="map">';
    pendingVisitsTemplate += '<i class="fa fa-user"></i>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<div class="company">';
    pendingVisitsTemplate += '<div class="cname">Personal</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</a>';
    pendingVisitsTemplate += '</li>';

    pendingVisitsTemplate += '</ul>	';
    pendingVisitsTemplate += '</p>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '<div class="titl">' + title + '</div>';
    if (description !== "")
    {
        pendingVisitsTemplate += '<div class="dscp">Description: ' + description + '</div>';
    }
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</div>';
    pendingVisitsTemplate += '</div>';
    return pendingVisitsTemplate;
}

function NodataAvailable(TimeStart, TimeEnd, Time_noData, message)
{
//    alert("NodataAvailable");
    var NoDataAvailableTemplate = '';
    NoDataAvailableTemplate += '<div class="container_time right unplan_comp">';
    NoDataAvailableTemplate += '<div class="content_time">';
    NoDataAvailableTemplate += '<div class="cnt_dtl">';

    NoDataAvailableTemplate += '<h5>GPS OFF <span>' + TimeStart + '  -  ' + TimeEnd + '<br/>(' + Time_noData + ')</span></h5>';


    NoDataAvailableTemplate += '<div class="dropdown" id="switch-boards">';

    NoDataAvailableTemplate += '<p><a href="#">' + message + '</a>';


    NoDataAvailableTemplate += '</p>';
    NoDataAvailableTemplate += '</div>';
    NoDataAvailableTemplate += '</div>';
    NoDataAvailableTemplate += '</div>';
    NoDataAvailableTemplate += '</div>';
    return NoDataAvailableTemplate;
//$$$$$$$$$$$$$$$$$$$$$
}

function visitCancel(startTime, endTime, VisitDuration, customername, title, description)
{
    var visitCancelTemplate = '';
    visitCancelTemplate += '<div class="container_time travel_arrow visit_skip">';
    visitCancelTemplate += '<div class="content_time3">';
    visitCancelTemplate += '<div class="cnt_dtl">';
    visitCancelTemplate += '<h5>02 SCHEDULED VISIT <span>' + startTime + '  -  ' + endTime + '<br/>(' + VisitDuration + ')</span></h5>';
    visitCancelTemplate += '<p><a nohref>' + customername + '</a></p>';
    visitCancelTemplate += '<div class="titl">' + title + '</div>';
    if (description !== "")
    {
        visitCancelTemplate += '<div class="dscp">Description: ' + description + '</div>';
    }
    visitCancelTemplate += '</div>';
    visitCancelTemplate += '</div>';
    visitCancelTemplate += '</div>';
    return visitCancelTemplate;

}

function noDataForDay()
{
    var testData = "";
    testData += '<div id="trailDiv">'; //20sep
    testData += "<div class='targetDiv'>";
    testData += "<div class='productivity'>";
    testData += "<div class='portlet-title'>";

    testData += "<div class='pic_block'>";
    testData += "<div class='avatar'>";
//testData +="<img alt='' src='assets_TimeLine/img/avatar1_small.jpg'>";
    testData += '<img alt="" src="' + imageURLPath + accountId + '_' + loginUserId + '_29X29.png?' + time + '" onerror="this.src=\'assets/img/usrsml_29.png\';"/>';
    testData += "</div>";
    testData += "<div class='info'>";
    testData += "<div class='caption'>Arunkumar Poojary</div>";
    testData += "<div class='caption2'>Punched-Out @ 7:20 pm</div>";
    testData += "<div class='location'><img alt='' src='assets_TimeLine/img/loc.png'>Mumbai @14:00PM</div>";
    testData += "</div>";
    testData += "</div>";
    testData += "<div class='phone_details'>";
    testData += "<ul class='nav navbar-nav pull-right'>";
    testData += "<li class='dropdown' id='header_notification_bar'>";
    testData += "<a href='#'  class='dropdown-toggle' data-toggle='dropdown' data-click='dropdown' data-close-others='true'>";
    testData += "<div class='phone_dtl'></div>";
    testData += "</a>";
    testData += "<ul class='dropdown-menu extended notification'>";
    testData += "<li>";
    testData += "<p>";
    testData += "Phone Details";
    testData += "</p>";
    testData += "</li>";
    testData += "<li>";
    testData += "<div>";
    testData += "<meter style='width: 24px' max='100' low='35' high='55' optimum='100' value='10'></meter>";
    testData += "<span class='time error'>";
    testData += "10%";
    testData += "</span>";
    testData += "</div>";
    testData += "</li>";
    testData += "<li>";
    testData += "<div>";
    testData += "<img alt='' src='assets_TimeLine/img/gps-off.png'>";
    testData += "<span class='time error'>";
    testData += "GPS OFF";
    testData += "</span>";
    testData += "</div>";
    testData += "</li>";
    testData += "<li>";
    testData += "<div>";
    testData += "<img alt='' src='assets_TimeLine/img/network.png'>";
    testData += "<span class='time'>";
    testData += "4G";
    testData += "</span>";
    testData += "</div>";
    testData += "</li>";
    testData += "<li>";
    testData += "<div>";
    testData += "<img alt='' src='assets_TimeLine/img/fs-pd.png'>";
    testData += "<span class='time'>";
    testData += "v2.6.4";
    testData += "</span>";
    testData += "</div>";
    testData += "</li>";
    testData += "<li>";
    testData += "<div>";
    testData += "<img alt='' src='assets_TimeLine/img/android.png'>";
    testData += "<span class='time'>";
    testData += "v2.15";
    testData += "</span>";
    testData += "</div>";
    testData += "</li>";
    testData += "<li>";
    testData += "<div>";
    testData += "<img alt='' src='assets_TimeLine/img/device.png'>";
    testData += "<span class='time'>";
    testData += "Samsung";
    testData += "</span>";
    testData += "</div>";
    testData += "</li>";
    testData += "</ul>";
    testData += "</li>								";

    testData += "</ul>";
    testData += "</div>";

    testData += "</div>";
    testData += "<div class='prod-title'>";
    testData += "<div class='prod_block'>";
    testData += "<div class='prod'>";
    testData += "<div class='arc'>";
    testData += "<div class='GaugeMeter' id='PreviewGaugeMeter_2' data-percent='23.56' data-append=' %' data-size='58' data-theme='White' data-back='RGBa(0,0,0,.2)' data-animate_gauge_colors=true data-animate_text_colors=true data-width='2' data-label='' data-label_color='#FFF'></div>										";
    testData += "<span>Productivity</span>";
    testData += "</div></div>";
    testData += "<div class='prod2'>";
    testData += "<div class='arc2'><img alt='' src='assets_TimeLine/img/visit.png'></div><span class='count'>2</span> <span>Visits</span>";
    testData += "<div class='arc_time'>1 hr 12 min</div>";
    testData += "</div>";

    testData += "<div class='prod2'>";
    testData += "<div class='arc2'><img alt='' src='assets_TimeLine/img/kms.png'></div><span class='count'>11</span> <span>kms</span>";
    testData += "<div class='arc_time'>1 hr 12 min</div>";
    testData += "</div>";

    testData += "<div class='prod2'>";
    testData += "<div class='arc2'><img alt='' src='assets_TimeLine/img/office.png'></div><span class='count'></span> <span>Office</span>";
    testData += "<div class='arc_time'>1 hr 12 min</div>";
    testData += "</div>";
    testData += "</div>";
    testData += "</div>";
    testData += "<div class='msg-box'>";
    testData += "<form class='form-horizontal' role='form' onsubmit='return false'>";
    testData += "<div class='form-group'>";
    testData += "<div class='col-md-8'>";
    testData += "<div class='input-icon'>";
    testData += "<i class='fa fa-comment fa-flip-horizontal'></i>";
    testData += "<input type='search' class='form-control' placeholder='Type Message' style='width:263px;'>";
    testData += "</div>									";
    testData += "</div>";
    testData += "<div class='col-md-4'><button type='button' class='btn btn-sm btn-info' style='padding: 7px 12px;;float:right;' onclick='messageSendTemplate('70');return false;'> SEND</button></div>";
    testData += "</div>";
    testData += "</form>							";
    testData += "</div>";
    testData += "</div>";
    testData += '</div>'; //20 sep

    testData += "<div class='portlet-body'>";

//testData +="<div class='date_block'>";
//testData +="<div class='input-group date date-picker input-small' data-date-format='dd M yyyy'>";
//testData +="<input type='text' disabled class='form-control' value='21 Feb 2018'>";
//testData +="<span class='input-group-btn'>";
//testData +="<button class='btn btn-default' type='button'><i class='fa fa-calendar'></i></button>";
//testData +="</span>";
//testData +="</div>";
//testData +="</div>";

    testData += '<div class="date_block">';
    testData += '<div class="input-group date date-picker input-small" data-date-format="dd M yyyy">';
    testData += '<input type="text" disabled class="form-control" value="" id="id_activityForDate">';  //nikhilCalender
    testData += '<span class="input-group-btn">';
    testData += '<button class="btn btn-default" type="button"><i class="fa fa-calendar"></i></button>';
    testData += '</span>';
    testData += '</div>';
    testData += '</div>';

    testData += "<div class='timeline_title down_arrow'>TIMELINE</div>";

    testData += '<div class="scroller" data-always-visible="1" data-rail-visible1="1">	';
    testData += '<div class="timeline">';
    testData += "</div>";
    testData += "</div>";

    testData += "</div>";
    testData += "</div>";

    return testData;
//          $(".GaugeMeter").gaugeMeter();
//           $("#GaugeMeter_103").gaugeMeter({percent:15});

}
