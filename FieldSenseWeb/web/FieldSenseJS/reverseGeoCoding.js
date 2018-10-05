/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function reverseGeoCoding(latitude, longitude, callback) {
    var geocoder = new google.maps.Geocoder;
    var latlng = {lat: parseFloat(latitude), lng: parseFloat(longitude)};
    geocoder.geocode({'location': latlng}, function (results, status) {
        if (status === 'OK') {
            if (results[0]) {
                console.log("reverseGeoCoding : " + results[0].formatted_address);
                callback(results[0].formatted_address);
            } else {
                console.log('No results found');
                callback("");
            }
        } else {
            console.log('Geocoder failed due to: ' + status);
            callback("");
        }
    });
}