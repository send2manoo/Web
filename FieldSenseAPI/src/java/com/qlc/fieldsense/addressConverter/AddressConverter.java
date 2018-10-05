/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qlc.fieldsense.addressConverter;

import com.qlc.fieldsense.utils.Constant;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Ramesh
 * @date 18-08-2014
 */
public class AddressConverter {
    /*
     * Geocode request URL. Here see we are passing "json" it means we will get
     * the output in JSON format. You can also pass "xml" instead of "json" for
     * XML output. For XML output URL will be
     * "http://maps.googleapis.com/maps/api/geocode/xml";
     */

    private static final String URL = "https://maps.googleapis.com/maps/api/geocode/json";

    /*
     * Here the fullAddress String is in format like
     * "address,city,state,zipcode". Here address means "street number + route"
     * .
     */

    /**
     *
     * @param fullAddress
     * @return
     * @throws IOException
     */
    
    public GoogleResponse convertToLatLong(String fullAddress) throws IOException {

        /*
         * Create an java.net.URL object by passing the request URL in
         * constructor. Here you can see I am converting the fullAddress String
         * in UTF-8 format. You will get Exception if you don't convert your
         * address in UTF-8 format. Perhaps google loves UTF-8 format. :) In
         * parameter we also need to pass "sensor" parameter. sensor (required
         * parameter) — Indicates whether or not the geocoding request comes
         * from a device with a location sensor. This value must be either true
         * or false.
         */
        URL url = new URL(URL + "?address="
                + URLEncoder.encode(fullAddress, "UTF-8") + "&sensor=false");
        // Open the Connection
        URLConnection conn = url.openConnection();

        InputStream in = conn.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        GoogleResponse response = (GoogleResponse) mapper.readValue(in, GoogleResponse.class);
        in.close();
        return response;


    }
    
    /**
     *
     * @param lat
     * @param longi
     * @return
     * @throws IOException
     */
    public GoogleResponse convertToAddressFromLatLong(String lat,String longi) throws IOException {

        /*
         * Create an java.net.URL object by passing the request URL in
         * constructor. Here you can see I am converting the fullAddress String
         * in UTF-8 format. You will get Exception if you don't convert your
         * address in UTF-8 format. Perhaps google loves UTF-8 format. :) In
         * parameter we also need to pass "sensor" parameter. sensor (required
         * parameter) — Indicates whether or not the geocoding request comes
         * from a device with a location sensor. This value must be either true
         * or false.
         */
        
        URL url = new URL(URL + "?latlng="+lat+","+longi+"&key="+Constant.API_GOOGLE_GEOCODING_KEY); //Key added by awaneesh
        // Open the Connection
        URLConnection conn = url.openConnection();

        InputStream in = conn.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        GoogleResponse response = (GoogleResponse) mapper.readValue(in, GoogleResponse.class);
        in.close();
        return response;

    }
    
    /*
    public String convertToAddressFromLatLong1(String lat,String longi)  {
        String address="";
        try {
            
            String url=URL+"?latlng="+lat+","+longi;
            String str="";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            con.setRequestMethod("GET");
//            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            // Send post request
            con.setDoOutput(true);

            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
            StringBuilder sb = new StringBuilder();
	    String output;
	    while ((output = br.readLine()) != null) {
		sb.append(output.trim());
            }

           // build a JSON object
	    JSONObject obj1 = new JSONObject(sb.toString());
	    if (! obj1.getString("status").equals("OK"))
		return "";
	 
	    // get the first result
	    JSONObject res = obj1.getJSONArray("results").getJSONObject(0);
	  //  System.out.println("Address:"+res.getString("formatted_address"));
            address=res.getString("formatted_address");
            
	    /*JSONObject loc =
		res.getJSONObject("geometry").getJSONObject("location");
//	    System.out.println("lat: " + loc.getDouble("lat") +
		                ", lng: " + loc.getDouble("lng"));*//*
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return address;
    }
    */

    //}
    

//    public static void main(String[] args) throws IOException {
//
//        GoogleResponse res = new AddressConverter().convertToLatLong("xxabx");
//        if (res.getStatus().equals("OK")) {
//            for (Result result : res.getResults()) {
//                System.out.println("Lattitude of address is :" + result.getGeometry().getLocation().getLat());
//                System.out.println("Longitude of address is :" + result.getGeometry().getLocation().getLng());
//                System.out.println("Location is " + result.getGeometry().getLocation_type());
//            }
//        }
//    }
}
