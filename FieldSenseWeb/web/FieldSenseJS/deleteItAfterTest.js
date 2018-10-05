/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function createAccTemplate() {
    //Getting current date
    //siddhesh
   //`1 alert("createAccTemplate++++");
    var currentDate = new Date();
    var dd = currentDate.getDate();
    var mm = currentDate.getMonth() + 1; //January is 0!
    var yyyy = currentDate.getFullYear();

    if (dd < 10) {
        dd = '0' + dd;
    }

    if (mm < 10) {
        mm = '0' + mm;
    }

    currentDate = dd + '/' + mm + '/' + yyyy;
    mm = mm - 1;
    var dateOfDefaultExpiry = new Date(yyyy, mm, dd);
    dateOfDefaultExpiry = new Date(new Date(dateOfDefaultExpiry).setMonth(dateOfDefaultExpiry.getMonth() + 12));
    dd = dateOfDefaultExpiry.getDate() - 1;
    mm = dateOfDefaultExpiry.getMonth() + 1;
    yyyy = dateOfDefaultExpiry.getFullYear();
    if (dd < 10) {
        dd = '0' + dd;
    }

    if (mm < 10) {
        mm = '0' + mm;
    }
    dateOfDefaultExpiry = dd + '/' + mm + '/' + yyyy;
    //end 
    var accStatusTemplate = '';
//    accStatusTemplate += '<div id="acc_cre" class="modal fade" tabindex="-1"aria-hidden="true">';
    accStatusTemplate += '<div class="modal-dialog"  id="id_accStatus">';
    accStatusTemplate += '<div class="modal-content">';
    accStatusTemplate += '<div class="modal-header">';
    accStatusTemplate += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"><button>		';
    accStatusTemplate += '<h4 class="modal-title">Create Account</h4>';


    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="modal-body">';
    accStatusTemplate += '<div class="tabbable tabbable-custom">';
    accStatusTemplate += '<ul class="nav nav-tabs">';
    accStatusTemplate += '<li class="active">';
    accStatusTemplate += '<a href="#tab_1_0" data-toggle="tab">Admin</a>';

    accStatusTemplate += '</li>';
    accStatusTemplate += '<li class="">';
    accStatusTemplate += '<a href="#tab_1_1" data-toggle="tab">Plan</a>';
    accStatusTemplate += '</li>';
    accStatusTemplate += '<li class="">';
    accStatusTemplate += '<a href="#tab_1_2" data-toggle="tab">Account</a>';
    accStatusTemplate += '</li>';
    accStatusTemplate += '</ul>';
    accStatusTemplate += '<div class="tab-content">';
    accStatusTemplate += '<div class="tab-pane fade active in" id="tab_1_0">';
    accStatusTemplate += '<div class="form-body" id="act">';
    accStatusTemplate += '<form action="#" class="form-horizontal">';
    accStatusTemplate += '<div class="table-toolbar">';
    accStatusTemplate += '<div class="btn-group">';
    accStatusTemplate += '<button class="btn btn-info" id="sample_editable_1_new" ">';
    accStatusTemplate += '<i class="fa fa-plus"></i> Add New';
    accStatusTemplate += '</button>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<table class="table table-striped table-hover table-bordered" id="sample_editable_1">';
    accStatusTemplate += '<thead>';
    accStatusTemplate += '<tr>';
    accStatusTemplate += '<th>';
    accStatusTemplate += 'Name';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    accStatusTemplate += 'Email';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    accStatusTemplate += 'Password';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    accStatusTemplate += 'Designation';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    accStatusTemplate += 'Mobile';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';
    accStatusTemplate += 'Gender';
    accStatusTemplate += '</th>';
    accStatusTemplate += '<th>';

    accStatusTemplate += '</th>';
    accStatusTemplate += '</tr>';
    accStatusTemplate += '</thead>';
    accStatusTemplate += '<tbody>';
    accStatusTemplate += '<tr>';
    accStatusTemplate += '<td>';
    accStatusTemplate += ' <input name="first_name" value="Alex" size="6" id="id_uname" readonly >';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td>';
    accStatusTemplate += '<input name="last_name" value="Alex@ratanfire.com" size="10" id="id_email" readonly>';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td>';
    accStatusTemplate += '<input name="pass" value="********" size="8" id="id_pwd" readonly> ';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td>';
    accStatusTemplate += '<input name="designation" value="Manager - Support" size="10" id="id_designation" readonly>';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td>';
    accStatusTemplate += '<input name="mobile_no" value="9819245678" size="10" id="id_mobile_number" readonly>';
    accStatusTemplate += '</td>';
    accStatusTemplate += '<td>';
    accStatusTemplate += '<input name="gender" value="Male" size="8" id="id="id_gender_radiobtns"" readonly> ' ;
    accStatusTemplate += '</td>';
    
    accStatusTemplate += '<td>';
    accStatusTemplate += '<button class=" " id="btnEdit" onclick="writeonly()" > edit </button> ' ;
//    accStatusTemplate += '<button class=" " id="btnCancel" onclick="readonly()" > cancel </button> ' ;
    accStatusTemplate += '</td>';
    
//    accStatusTemplate += '<td>';
//    accStatusTemplate += '<a class="edit" href="javascript:;">Edit</a>';
//    accStatusTemplate += '<a class="delete" href="javascript:;">Delete</a>';
//    accStatusTemplate += '</td>';
    accStatusTemplate += '</tr>';
    accStatusTemplate += '</tbody>';
    accStatusTemplate += '</table>';
    accStatusTemplate += '</form>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="tab-pane fade" id="tab_1_1">';
    accStatusTemplate += '<div class="form-body" id="act">';
    accStatusTemplate += '<form action="#" class="form-horizontal">';

    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">	';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Plan</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    //id_plan
    accStatusTemplate += '<select name="region" id="id_plan" class="form-control select2"><option value="0">Select</option><option value="1">Free</option><option value="2">';
    accStatusTemplate += 'Premium';
    accStatusTemplate += '</option>';
    accStatusTemplate += '</select>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">User Limit</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="" id="id_userLimit">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';

    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Start Date</label>';
    accStatusTemplate += '<div class="col-md-8">';
    accStatusTemplate += '<div class="input-group date date-picker" data-date="20 May 2017" data-date-format="dd MM yyyy" data-date-viewmode="years">';
    accStatusTemplate += '<input type="text" class="form-control" id="start_date" readonly>';
    accStatusTemplate += '<span class="input-group-btn">';
    accStatusTemplate += '<button class="btn btn-info" type="button"><i class="fa fa-calendar"></i></button>';
    accStatusTemplate += '</span>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Expiry Date</label>';
    accStatusTemplate += '<div class="col-md-8">';
    accStatusTemplate += '<div class="input-group date date-picker" data-date="19 August 2017" data-date-format="dd MM yyyy" data-date-viewmode="years">';
    accStatusTemplate += '<input type="text" class="form-control" value="19 August 2017" id="expiry_date" readonly>';
    accStatusTemplate += '<span class="input-group-btn">';
    accStatusTemplate += '<button class="btn btn-info" type="button"><i class="fa fa-calendar"></i></button>';
    accStatusTemplate += '</span>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>	';

    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</form>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="tab-pane fade" id="tab_1_2">';
    accStatusTemplate += '<div class="form-body" id="act">';
    accStatusTemplate += '<form action="#" class="form-horizontal">';
    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-12">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-3 control-label required">Status</label>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="radio-list">';
    accStatusTemplate += '<label class="radio-inline">';
    accStatusTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios4" value="option1" checked> Active </label>';
    accStatusTemplate += '<label class="radio-inline">';
    accStatusTemplate += '<input type="radio" name="optionsRadios" id="optionsRadios5" value="option2"> Inactive </label>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Name</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="" id="id_companyName">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Phone</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="Phone" class="form-control" placeholder="" value="" id="id_comPnNo1">';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '<div class="row">	';
    accStatusTemplate += '<div class="col-xs-6">	';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Address</labe';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<textarea class="form-control" rows="3" placeholder="" id="id_address1" ></textarea>';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '</div>	';
    accStatusTemplate += '<div class="col-xs-6">	';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">City</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" value="" id="id_city">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="row">	';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label">State</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="Phone1" class="form-control" placeholder="" value="" id="id_state">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Country</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<select name="" id="select2_sample4" class="form-control select2">';
    accStatusTemplate += '<option value="select">--Select Country--</option>';
    accStatusTemplate += '<option value="Afghanistan">Afghanistan</option>';
    accStatusTemplate += '<option value="Albania">Albania</option>';
    accStatusTemplate += '<option value="Algeria">Algeria</option>';
    accStatusTemplate += '<option value="American Samoa">American Samoa</option>';
    accStatusTemplate += '<option value="Andorra">Andorra</option>';
    accStatusTemplate += '<option value="Angola">Angola</option>';
    accStatusTemplate += '<option value="Anguilla">Anguilla</option>';
    accStatusTemplate += '<option value="Antarctica">Antarctica</option>';
    accStatusTemplate += '<option value="Argentina">Argentina</option>';
    accStatusTemplate += '<option value="Armenia">Armenia</option>';
    accStatusTemplate += '<option value="Aruba">Aruba</option>';
    accStatusTemplate += '<option value="Australia">Australia</option>';
    accStatusTemplate += '<option value="Austria">Austria</option>';
    accStatusTemplate += '<option value="Azerbaijan">Azerbaijan</option>';
    accStatusTemplate += '<option value="Bahamas">Bahamas</option>';
    accStatusTemplate += '<option value="Bahrain">Bahrain</option>';
    accStatusTemplate += '<option value="Bangladesh">Bangladesh</option>';
    accStatusTemplate += '<option value="Barbados">Barbados</option>';
    accStatusTemplate += '<option value="Belarus">Belarus</option>';
    accStatusTemplate += '<option value="Belgium">Belgium</option>';
    accStatusTemplate += '<option value="Belize">Belize</option>';
    accStatusTemplate += '<option value="Benin">Benin</option>';
    accStatusTemplate += '<option value="Bermuda">Bermuda</option>';
    accStatusTemplate += '<option value="Bhutan">Bhutan</option>';
    accStatusTemplate += '<option value="Bolivia">Bolivia</option>';
    accStatusTemplate += '<option value="Bosnia and Herzegowina">Bosnia and Herzegowina</option>';
    accStatusTemplate += '<option value="Bouvet Island">Bouvet Island</option>';
    accStatusTemplate += '<option value="Brazil">Brazil</option>';
    accStatusTemplate += '<option value="British Indian Ocean Territory">British Indian Ocean Territory</option>';
    accStatusTemplate += '<option value="Brunei Darussalam">Brunei Darussalam</option>';
    accStatusTemplate += '<option value="Bulgaria">Bulgaria</option>';
    accStatusTemplate += '<option value="Burkina Faso">Burkina Faso</option>';
    accStatusTemplate += '<option value="Burundi">Burundi</option>';
    accStatusTemplate += '<option value="Cambodia">Cambodia</option>';
    accStatusTemplate += '<option value="Cameroon">Cameroon</option>';
    accStatusTemplate += '<option value="Canada">Canada</option>';
    accStatusTemplate += '<option value="Cape Verde">Cape Verde</option>';
    accStatusTemplate += '<option value="Cayman Islands">Cayman Islands</option>';
    accStatusTemplate += '<option value="Central African Republic">Central African Republic</option>';
    accStatusTemplate += '<option value="Chad">Chad</option>';
    accStatusTemplate += '<option value="Chile">Chile</option>';
    accStatusTemplate += '<option value="China">China</option>';
    accStatusTemplate += '<option value="Christmas Island">Christmas Island</option>';
    accStatusTemplate += '<option value="Cocos (Keeling) Islands">Cocos (Keeling) Islands</option>';
    accStatusTemplate += '<option value="Colombia">Colombia</option>';
    accStatusTemplate += '<option value="Comoros">Comoros</option>';
    accStatusTemplate += '<option value="Congo">Congo</option>';
    accStatusTemplate += '<option value="Congo, the Democratic Republic of the">Congo, the Democratic Republic of the</option>';
    accStatusTemplate += '<option value="Cook Islands">Cook Islands</option>';
    accStatusTemplate += '<option value="Costa Rica">Costa Rica</option>';
    accStatusTemplate += '<option value="Cote dIvoire">Cote dIvoire</option>';
    accStatusTemplate += '<option value="Croatia (Hrvatska)">Croatia (Hrvatska)</option>';
    accStatusTemplate += '<option value="Cuba">Cuba</option>';
    accStatusTemplate += '<option value="Cyprus">Cyprus</option>';
    accStatusTemplate += '<option value="Czech Republic">Czech Republic</option>';
    accStatusTemplate += '<option value="Denmark">Denmark</option>';
    accStatusTemplate += '<option value="Djibouti">Djibouti</option>';
    accStatusTemplate += '<option value="Dominica">Dominica</option>';
    accStatusTemplate += '<option value="Dominican Republic">Dominican Republic</option>';
    accStatusTemplate += '<option value="Ecuador">Ecuador</option>';
    accStatusTemplate += '<option value="Egypt">Egypt</option>';
    accStatusTemplate += '<option value="El Salvador">El Salvador</option>';
    accStatusTemplate += '<option value="Equatorial Guinea">Equatorial Guinea</option>';
    accStatusTemplate += '<option value="Eritrea">Eritrea</option>';
    accStatusTemplate += '<option value="Estonia">Estonia</option>';
    accStatusTemplate += '<option value="Ethiopia">Ethiopia</option>';
    accStatusTemplate += '<option value="Falkland Islands (Malvinas)">Falkland Islands (Malvinas)</option>';
    accStatusTemplate += '<option value="Faroe Islands">Faroe Islands</option>';
    accStatusTemplate += '<option value="Fiji">Fiji</option>';
    accStatusTemplate += '<option value="Finland">Finland</option>';
    accStatusTemplate += '<option value="France">France</option>';
    accStatusTemplate += '<option value="French Guiana">French Guiana</option>';
    accStatusTemplate += '<option value="French Polynesia">French Polynesia</option>';
    accStatusTemplate += '<option value="French Southern Territories">French Southern Territories</option>';
    accStatusTemplate += '<option value="Gabon">Gabon</option>';
    accStatusTemplate += '<option value="Gambia">Gambia</option>';
    accStatusTemplate += ' <option value="Georgia">Georgia</option>';
    accStatusTemplate += '<option value="Germany">Germany</option>';
    accStatusTemplate += '<option value="Ghana">Ghana</option>';
    accStatusTemplate += '<option value="Gibraltar">Gibraltar</option>';
    accStatusTemplate += '<option value="Greece">Greece</option>';
    accStatusTemplate += '<option value="Greenland">Greenland</option>';
    accStatusTemplate += '<option value="Grenada">Grenada</option>';
    accStatusTemplate += '<option value="Guadeloupe">Guadeloupe</option>';
    accStatusTemplate += '<option value="Guam">Guam</option>';
    accStatusTemplate += '<option value="Guatemala">Guatemala</option>';
    accStatusTemplate += '<option value="Guinea">Guinea</option>';
    accStatusTemplate += '<option value="Guinea-Bissau">Guinea-Bissau</option>';
    accStatusTemplate += '<option value="Guyana">Guyana</option>';
    accStatusTemplate += '<option value="Haiti">Haiti</option>';
    accStatusTemplate += '<option value="Heard and Mc Donald Islands">Heard and Mc Donald Islands</option>';
    accStatusTemplate += '<option value="Holy See (Vatican City State)">Holy See (Vatican City State)</option>';
    accStatusTemplate += '<option value="Honduras">Honduras</option>';
    accStatusTemplate += '<option value="Hong Kong">Hong Kong</option>';
    accStatusTemplate += '<option value="Hungary">Hungary</option>';
    accStatusTemplate += '<option value="Iceland">Iceland</option>';
    accStatusTemplate += '<option value="India">India</option>';
    accStatusTemplate += '<option value="Indonesia">Indonesia</option>';
    accStatusTemplate += '<option value="Iran (Islamic Republic of)">Iran (Islamic Republic of)</option>';
    accStatusTemplate += '<option value="Iraq">Iraq</option>';
    accStatusTemplate += '<option value="Ireland">Ireland</option>';
    accStatusTemplate += '<option value="Israel">Israel</option>';
    accStatusTemplate += '<option value="Italy">Italy</option>';
    accStatusTemplate += '<option value="Jamaica">Jamaica</option>';
    accStatusTemplate += '<option value="Japan">Japan</option>';
    accStatusTemplate += '<option value="Jordan">Jordan</option>';
    accStatusTemplate += '<option value="Kazakhstan">Kazakhstan</option>';
    accStatusTemplate += '<option value="Kenya">Kenya</option>';
    accStatusTemplate += '<option value="Kiribati">Kiribati</option>';
    accStatusTemplate += '<option value="Korea, Democratic Peoples Republic of">Korea, Democratic Peoples 	Republic of</option>';
    accStatusTemplate += '<option value="Korea, Republic of">Korea, Republic of</option>';
    accStatusTemplate += '<option value="Kuwait">Kuwait</option>';
    accStatusTemplate += '<option value="Kyrgyzstan">Kyrgyzstan</option>';
    accStatusTemplate += '<option value="Lao Peoples Democratic Republic">Lao Peoples Democratic Republic</option>';
    accStatusTemplate += '<option value="Latvia">Latvia</option>';
    accStatusTemplate += '<option value="Lebanon">Lebanon</option>';
    accStatusTemplate += '<option value="Lesotho">Lesotho</option>';
    accStatusTemplate += '<option value="Liberia">Liberia</option>';
    accStatusTemplate += '<option value="Libyan Arab Jamahiriya<">Libyan Arab Jamahiriya</option>';
    accStatusTemplate += '<option value="Liechtenstein">Liechtenstein</option>';
    accStatusTemplate += '<option value="Lithuania">Lithuania</option>';
    accStatusTemplate += '<option value="Luxembourg">Luxembourg</option>';
    accStatusTemplate += '<option value="MO">Macau</option>';
    accStatusTemplate += '<option value="Macedonia, The Former Yugoslav Republic of">Macedonia, The Former Yugoslav Republic of</option>';
    accStatusTemplate += '<option value="Madagascar">Madagascar</option>';
    accStatusTemplate += '<option value="Malawi">Malawi</option>';
    accStatusTemplate += '<option value="Malaysia">Malaysia</option>';
    accStatusTemplate += '<option value="Maldives">Maldives</option>';
    accStatusTemplate += '<option value="Mali">Mali</option>';
    accStatusTemplate += '<option value="Malta">Malta</option>';
    accStatusTemplate += '<option value="Marshall Islands">Marshall Islands</option>';
    accStatusTemplate += '<option value="Martinique">Martinique</option>';
    accStatusTemplate += '<option value="Mauritania">Mauritania</option>';
    accStatusTemplate += '<option value="Mauritius">Mauritius</option>';
    accStatusTemplate += '<option value="Mayotte">Mayotte</option>';
    accStatusTemplate += '<option value="Mexico">Mexico</option>';
    accStatusTemplate += '<option value="Micronesia, Federated States of">Micronesia, Federated States of</option>';
    accStatusTemplate += '<option value=">Moldova, Republic of">Moldova, Republic of</option>';
    accStatusTemplate += '<option value="Monaco">Monaco</option>';
    accStatusTemplate += '<option value="Mongolia">Mongolia</option>';
    accStatusTemplate += '<option value="Montserrat">Montserrat</option>';
    accStatusTemplate += '<option value="Morocco">Morocco</option>';
    accStatusTemplate += '<option value="Mozambique">Mozambique</option>';
    accStatusTemplate += '<option value="Myanmar">Myanmar</option>';
    accStatusTemplate += '<option value="Namibia">Namibia</option>';
    accStatusTemplate += '<option value="Nauru">Nauru</option>';
    accStatusTemplate += '<option value="Nepal">Nepal</option>';
    accStatusTemplate += '<option value="Netherlands">Netherlands</option>';
    accStatusTemplate += '<option value="New Caledonia">New Caledonia</option>';
    accStatusTemplate += '<option value="New Zealand">New Zealand</option>';
    accStatusTemplate += '<option value="Nicaragua">Nicaragua</option>';
    accStatusTemplate += '<option value="Niger">Niger</option>';
    accStatusTemplate += '<option value="Nigeria">Nigeria</option>';
    accStatusTemplate += '<option value="Niue">Niue</option>';
    accStatusTemplate += '<option value="Norfolk Island">Norfolk Island</option>';
    accStatusTemplate += '<option value="Northern Mariana Islands">Northern Mariana Islands</option>';
    accStatusTemplate += '<option value="Norway">Norway</option>';
    accStatusTemplate += '<option value="Oman">Oman</option>';
    accStatusTemplate += '<option value="Pakistan">Pakistan</option>';
    accStatusTemplate += '<option value="Palau">Palau</option>';
    accStatusTemplate += '<option value="Panama">Panama</option>';
    accStatusTemplate += '<option value="Papua New Guinea">Papua New Guinea</option>';
    accStatusTemplate += '<option value="Paraguay">Paraguay</option>';
    accStatusTemplate += '<option value="Peru">Peru</option>';
    accStatusTemplate += '<option value="Philippines">Philippines</option>';
    accStatusTemplate += '<option value="Pitcairn">Pitcairn</option>';
    accStatusTemplate += '<option value="Poland">Poland</option>';
    accStatusTemplate += '<option value="Portugal">Portugal</option>';
    accStatusTemplate += '<option value="Puerto Rico">Puerto Rico</option>';
    accStatusTemplate += '<option value="Qatar">Qatar</option>';
    accStatusTemplate += '<option value="Reunion">Reunion</option>';
    accStatusTemplate += '<option value="Romania">Romania</option>';
    accStatusTemplate += '<option value="Russian Federation">Russian Federation</option>';
    accStatusTemplate += '<option value="Rwanda">Rwanda</option>';
    accStatusTemplate += '<option value="Saint Kitts and Nevis">Saint Kitts and Nevis</option>';
    accStatusTemplate += '<option value="Saint LUCIA">Saint LUCIA</option>';
    accStatusTemplate += '<option value="Saint Vincent and the Grenadines">Saint Vincent and the Grenadines</option>';
    accStatusTemplate += '<option value="Samoa">Samoa</option>';
    accStatusTemplate += '<option value="San Marino">San Marino</option>';
    accStatusTemplate += '<option value="Sao Tome and Principe">Sao Tome and Principe</option>';
    accStatusTemplate += '<option value="Saudi Arabia">Saudi Arabia</option>';
    accStatusTemplate += '<option value="Senegal">Senegal</option>';
    accStatusTemplate += '<option value="Seychelles">Seychelles</option>';
    accStatusTemplate += '<option value="Sierra Leone">Sierra Leone</option>';
    accStatusTemplate += '<option value="Singapore">Singapore</option>';
    accStatusTemplate += '<option value="Slovakia (Slovak Republic)">Slovakia (Slovak Republic)</option>';
    accStatusTemplate += '<option value="Slovenia">Slovenia</option>';
    accStatusTemplate += '<option value="Solomon Islands">Solomon Islands</option>';
    accStatusTemplate += '<option value="Somalia">Somalia</option>';
    accStatusTemplate += '<option value="South Africa">South Africa</option>';
    accStatusTemplate += '<option value="South Georgia and the South Sandwich Islands">South Georgia and the South Sandwich Islands</option>';
    accStatusTemplate += '<option value="Spain">Spain</option>';
    accStatusTemplate += '<option value="Sri Lanka">Sri Lanka</option>';
    accStatusTemplate += '<option value="St. Helena">St. Helena</option>';
    accStatusTemplate += '<option value="St. Pierre and Miquelon">St. Pierre and Miquelon</option>';
    accStatusTemplate += '<option value="Sudan">Sudan</option>';
    accStatusTemplate += '<option value="Suriname">Suriname</option>';
    accStatusTemplate += '<option value="Svalbard and Jan Mayen Islands">Svalbard and Jan Mayen Islands</option>';
    accStatusTemplate += '<option value="Swaziland">Swaziland</option>';
    accStatusTemplate += '<option value="Sweden">Sweden</option>';
    accStatusTemplate += '<option value="Switzerland">Switzerland</option>';
    accStatusTemplate += '<option value="Syrian Arab Republic">Syrian Arab Republic</option>';
    accStatusTemplate += '<option value="Taiwan, Province of China">Taiwan, Province of China</option>';
    accStatusTemplate += '<option value="TJ">Tajikistan</option>';
    accStatusTemplate += '<option value="Tajikistan">Tajikistan</option>';
    accStatusTemplate += '<option value="Tanzania, United Republic of">Tanzania, United Republic of</option>';
    accStatusTemplate += '<option value="Thailand">Thailand</option>';
    accStatusTemplate += '<option value="Togo">Togo</option>';
    accStatusTemplate += '<option value="Tokelau">Tokelau</option>';
    accStatusTemplate += '<option value="Tonga">Tonga</option>';
    accStatusTemplate += '<option value="Trinidad and Tobago">Trinidad and Tobago</option>';
    accStatusTemplate += '<option value="Tunisia">Tunisia</option>';
    accStatusTemplate += '<option value="Turkey">Turkey</option>';
    accStatusTemplate += '<option value="Turkmenistan">Turkmenistan</option>';
    accStatusTemplate += '<option value="Turks and Caicos Islands">Turks and Caicos Islands</option>';
    accStatusTemplate += '<option value="Tuvalu">Tuvalu</option>';
    accStatusTemplate += '<option value="Ukraine">Ukraine</option>';
    accStatusTemplate += '<option value="United Arab Emirates">United Arab Emirates</option>';
    accStatusTemplate += '<option value="United Kingdom">United Kingdom</option>';
    accStatusTemplate += '<option value="United States">United States</option>';
    accStatusTemplate += '<option value="United States Minor Outlying Islands">United States Minor Outlying Islands</option>';
    accStatusTemplate += '<option value="Uruguay">Uruguay</option>';
    accStatusTemplate += '<option value="Uzbekistan">Uzbekistan</option>';
    accStatusTemplate += '<option value="Vanuatu">Vanuatu</option>';
    accStatusTemplate += '<option value="Venezuela">Venezuela</option>';
    accStatusTemplate += '<option value="Viet Nam">Viet Nam</option>';
    accStatusTemplate += '<option value="Virgin Islands (British)">Virgin Islands (British)</option>';
    accStatusTemplate += '<option value="Virgin Islands (U.S.)">Virgin Islands (U.S.)</option>';
    accStatusTemplate += '<option value="Wallis and Futuna Islands">Wallis and Futuna Islands</option>';
    accStatusTemplate += '<option value="Western Sahara">Western Sahara</option>';
    accStatusTemplate += '<option value="Yemen">Yemen</option>';
    accStatusTemplate += '<option value="Zambia">Zambia</option>';
    accStatusTemplate += '<option value="Zimbabwe">Zimbabwe</option>';
    accStatusTemplate += '</select>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';

    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Zip Code</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="" id="id_zipCode">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label required">Region</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<select name="region" id="select2_region" class="form-control select2">';
    accStatusTemplate += '<option value="0">Select</option>';
    accStatusTemplate += '<option value="1">Mumbai</option>';
    accStatusTemplate += '<option value="2">North</option>';
    accStatusTemplate += '<option value="3">South</option>';
    accStatusTemplate += '<option value="4">West</option>';
    accStatusTemplate += '<option value="5">Gujarat</option>';
    accStatusTemplate += '<option value="6">East</option>';
    accStatusTemplate += '<option value="7">International</option>';
    accStatusTemplate += '</select>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';

    accStatusTemplate += '<div class="row">';
    accStatusTemplate += '<div class="col-xs-6">';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label">Website</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="" id="id_website">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';

    accStatusTemplate += '<div class="col-xs-6">	';
    accStatusTemplate += '<div class="form-group">';
    accStatusTemplate += '<label class="col-xs-4 control-label">Industry</label>';
    accStatusTemplate += '<div class="col-xs-8">';
    accStatusTemplate += '<input type="text" class="form-control" placeholder="" value="" id="id_industry">';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</form>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '<div class="modal-footer">';
    accStatusTemplate += '<button type="button" data-dismiss="modal" class="btn btn-default">Close</button>';
    accStatusTemplate += '<button type="button" data-dismiss="modal" class="btn btn-primary" onClick="createAcoount()">Save All</button>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
    accStatusTemplate += '</div>';
//    accStatusTemplate += '</div>';
    $('#responsive2').html(accStatusTemplate);
    App.initUniform();
    App.fixContentHeight();
    $('.date-picker').datepicker({
        rtl: App.isRTL(),
        autoclose: true
    });
    //App.callHandleScrollers();

}

function createAcoount() {
    alert("createAcoount");
    //Editing for start date feature.
    var startDate = $("#start_date").val();
    alert("startDate "+startDate);
    var expiredOn = $("#expiry_date").val();
    alert("expiredOn "+expiredOn);
    var sStartDate = dateConverter(startDate);
    var sExpiredOn = dateConverter(expiredOn);

//     var dateSplit = startDate.split("/");
//    var date = dateSplit[0];
//    var month = dateSplit[1];
//    var year = dateSplit[2];
//    //var sExpireDate=year+"-"+month+"-"+date+" "+"18:30:00" ;
//   var toDate = convertLocalDateToServerDate(year,month - 1,date, 23, 59);
//    month = toDate.getMonth() + 1;
//    if (month < 10) {
//        month = '0' + month;
//    }
//    date = toDate.getDate();
//    if (date < 10) {
//        date = '0' + date;
//    }
//    hours = toDate.getHours();
//    if (hours < 10) {
//        hours = '0' + hours;
//    }
//    minutes = toDate.getMinutes();
//    if (minutes < 10) {
//        minutes = '0' + minutes;
//    }
//    var sStartDate = toDate.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':59';

    var companyName = document.getElementById("id_companyName").value.trim();
    alert();
    if (companyName.length == 0) {
        fieldSenseTosterError("Company Name can't be empty .", true);
        return false;
    }
    var userLimit = document.getElementById("id_userLimit").value.trim();
    alert();
    if (isPostiveInteger(userLimit) == false) {
        fieldSenseTosterError("Please Enter Valid User Limit .", true);
        return false;
    }
    if (userLimit < 25) {
        fieldSenseTosterError("Minimum User Limit should be 25.", true);
        return false;
    }
    if (startDate === "" || startDate === " " || startDate === 0)
    {
        fieldSenseTosterError("Start Date should not be blank", true);
        return false;
    }
    if (expiredOn === "" || expiredOn === " " || expiredOn === 0)
    {
        fieldSenseTosterError(" Expiry Date should not be blank", true);
        return false;
    }
    if (sStartDate > sExpiredOn)
    {
        fieldSenseTosterError("Start date should not be greater than Expiry date", true);
        return false;
    }
    var address1 = document.getElementById("id_address1").value.trim();
    alert();
    if (address1.length == 0) {
        fieldSenseTosterError("Address can't be empty .", true);
        return false;
    }

    var regionId = document.getElementById("select2_region").value.trim();
    if (regionId == 0) {
        fieldSenseTosterError("Please Select Region .", true);
        return false;
    }

    var city = document.getElementById("id_city").value.trim();
    alert();
    if (city.length == 0) {
        fieldSenseTosterError("City can't be empty .", true);
        return false;
    }
    var state = document.getElementById("id_state").value.trim();
    alert();
    if (state.length == 0) {
        fieldSenseTosterError("State can't be empty .", true);
        return false;
    }
    var indexOfCountry = document.getElementById("select2_sample4");
    
    var country = indexOfCountry.options[indexOfCountry.selectedIndex].text;
    alert();
    if (country.length == 0 || country == "--Select Country--") {
        fieldSenseTosterError("Country should be selected .", true);
        return false;
    }
    var zipCode = document.getElementById("id_zipCode").value.trim();
    alert("zipCode"+zipCode);
    var phNo1 = document.getElementById("id_comPnNo1").value.trim();
    alert("phNo1"+phNo1);
    if (phNo1.length == 0) {
        fieldSenseTosterError("phone number can't be empty .", true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(phNo1);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid phone number.", true);
        return false;
    }
    var emailId = document.getElementById("id_email").value.trim();
    alert("emailId"+emailId);
    if (emailId.length == 0) {
        fieldSenseTosterError("Email can't be empty .", true);
        return false;
    }
    var emailPattern = /^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
    if (!emailPattern.test(emailId)) {
        fieldSenseTosterError("Invalid email address .", true);
        return false;
    }
    var password = document.getElementById("id_pwd").value;
    alert("password"+password);
    if (password.length == 0) {
        fieldSenseTosterError("Password can't be empty .", true);
        return false;
    }
    if (password.length < 8 || password.length > 20) {
        fieldSenseTosterError("Password length should be 8 to 20 characters .", true);
        return false;
    }
    var confirmPassword = document.getElementById("id_conpwd").value.trim();
    if (confirmPassword.length == 0) {
        fieldSenseTosterError("Re-type Your Password can't be empty .", true);
        return false;
    }
    if (password != confirmPassword) {
        fieldSenseTosterError("Confirm password does not match with password .", true);
        return false;
    }
    var userName = document.getElementById("id_uname").value.trim();
     alert("userName"+userName);
    if (userName.length == 0) {
        fieldSenseTosterError("User name can't be empty .", true);
        return false;
    }
    var designation = document.getElementById(" ").value.trim();
     alert("designation"+designation);
    if (designation.length == 0) {
        fieldSenseTosterError("User designation can't be empty .", true);
        return false;
    }
    var mobile = document.getElementById("id_mobile_number").value.trim();
    alert("mobile"+mobile);
    if (mobile.trim().length != 0) {
        if (!(/^\d+$/.test(mobile))) {
            fieldSenseTosterError("Invalid Mobile number.", true);
            return false;
        } else if (mobile.trim().length > 20) {
            fieldSenseTosterError("Mobile number should not be more than 20 digits", true);
            return false;
        }
    }
    var gender = $("input:radio[name=id_gender]:checked").val();
    alert("gender"+gender);
    if (gender == undefined) {
        fieldSenseTosterError("Please select the gender.", true);
        return false;
    }
    var userObject = {
        "firstName": userName,
        "designation": designation,
        "emailAddress": emailId,
        "password": password,
        "gender": gender,
        "mobileNo": mobile,
        "role": 1,
        "active": 1,
        "accountContactType": 1
    }
    var accountObject = {
        "companyName": companyName,
        "userLimit": userLimit,
        "regionId": regionId,
        "address1": address1,
        "city": city,
        "state": state,
        "country": country,
        "zipCode": zipCode,
        "companyPhoneNumber1": parseFloat(phNo1),
        "emailAddress": emailId,
        "strStartDate": sStartDate,
        "sExpiredOn": sExpiredOn,
        "user": userObject
    }
//    alert("accountObject"+accountObject);
//    var jsonData = JSON.stringify(accountObject);
//    $('#pleaseWaitDialog').modal('show');
//    $.ajax({
//        type: "POST",
//        url: fieldSenseURL + "/account",
//        contentType: "application/json; charset=utf-8",
//        headers: {
//            "userToken": userToken
//        },
//        crossDomain: false,
//        data: jsonData,
//        cache: false,
//        dataType: 'json',
//        asyn: false,
//        success: function (data) {
//            if (data.errorCode == '0000') {
//                fieldSenseTosterSuccess(data.errorMessage, true);
//                $("#responsive2").modal('hide');
//                clearFilters();
//            } else {
//                fieldSenseTosterError(data.errorMessage, true);
//                $('#pleaseWaitDialog').modal('hide');
//            }
//        },
//        error: function (xhr, status, error) {
//            var err = eval("(" + xhr.responseText + ")");
//            var message = err.fieldErrors[0].message;
//            fieldSenseTosterError(message, true);
//            $('#pleaseWaitDialog').modal('hide');
//        }
//    });
}

function editAccDetails(id, userId, offlineAccountState)
{
    var expiredOn = $("#date_value").val();
    var startDate = $("#start_date").val();
    var sExpireDate = dateConverter(expiredOn)
    var strStartDate = dateConverter(startDate);
//     var dateSplit = expiredOn.split("/");
//    var date = dateSplit[0];
//    var month = dateSplit[1];
//    var year = dateSplit[2];
//    //var sExpireDate=year+"-"+month+"-"+date+" "+"18:30:00" ;
//   var toDate = convertLocalDateToServerDate(year,month - 1,date, 23, 59);
//    month = toDate.getMonth() + 1;
//    if (month < 10) {
//        month = '0' + month;
//    }
//    date = toDate.getDate();
//    if (date < 10) {
//        date = '0' + date;
//    }
//    hours = toDate.getHours();
//    if (hours < 10) {
//        hours = '0' + hours;
//    }
//    minutes = toDate.getMinutes();
//    if (minutes < 10) {
//        minutes = '0' + minutes;
//    }
//    var sExpireDate = toDate.getFullYear() + "-" + month + "-" + date + " " + hours + ":" + minutes + ':59';

    var accStatus = $('input[name=optionsRadios3]:checked', '#edit-AccDetails').val();
    var companyName = document.getElementById("id_companyName").value.trim();
    if (companyName.length == 0) {
        fieldSenseTosterError("Company Name can't be empty .", true);
        return false;
    }

    var userLimit = document.getElementById("id_userLimit").value.trim();
    if (isPostiveInteger(userLimit) == false) {
        fieldSenseTosterError("Please Enter Valid User Limit .", true);
        return false;
    }
    if (userLimit < 25) {
        fieldSenseTosterError("Minimum User Limit should be 25.", true);
        return false;
    }
    if (startDate === "" || startDate === " " || startDate === 0)
    {
        fieldSenseTosterError("Start Date should not be blank", true);
        return false;
    }
    if (expiredOn === "" || expiredOn === " " || expiredOn === 0)
    {
        fieldSenseTosterError(" Expiry Date should not be blank", true);
        return false;
    }

    if (strStartDate > sExpireDate)
    {
        fieldSenseTosterError("Start date should not be greater than Expiry date", true);
        return false;
    }
    var address1 = document.getElementById("id_address1").value.trim();
    if (address1.length == 0) {
        fieldSenseTosterError("Address can't be empty .", true);
        return false;
    }
    var regionId = document.getElementById("select2_region").value.trim();
    if (regionId == 0) {
        fieldSenseTosterError("Please Select Region .", true);
        return false;
    }
    var city = document.getElementById("id_city").value.trim();
    if (city.length == 0) {
        fieldSenseTosterError("City can't be empty .", true);
        return false;
    }
    var state = document.getElementById("id_state").value.trim();
    if (state.length == 0) {
        fieldSenseTosterError("State can't be empty .", true);
        return false;
    }
    var indexOfCountry = document.getElementById("select2_sample_modal_4");
    var country = indexOfCountry.options[indexOfCountry.selectedIndex].text;
    if (country.length == 0) {
        fieldSenseTosterError("Country can't be empty .", true);
        return false;
    }
    var zipCode = document.getElementById("id_zipCode").value.trim();
    var phNo1 = document.getElementById("id_comPnNo1").value.trim();
    if (phNo1.length == 0) {
        fieldSenseTosterError("phone number can't be empty .", true);
        return false;
    }
    var isphNo1 = /^\d+$/.test(phNo1);
    if (!isphNo1) {
        fieldSenseTosterError("Invalid phone number.", true);
        return false;
    }

    // added by jyoti 21-12-2016
    var allowOffline_edit = 1;
    var valueAllowOffline_edit = document.getElementById("id_allowoffline_edit").checked;
    if (valueAllowOffline_edit == false) {
        allowOffline_edit = 0;
    }
    // ended by jyoti
    var accountObject = {
        "id": id,
        "userLimit": userLimit,
        "regionId": regionId,
        "status": accStatus,
        "companyName": companyName,
        "address1": address1,
        "city": city,
        "state": state,
        "country": country,
        "zipCode": zipCode,
        "companyPhoneNumber1": parseFloat(phNo1),
        "sExpiredOn": sExpireDate,
        "strStartDate": strStartDate
                // "user":userObject
    }
    var jsonData = JSON.stringify(accountObject);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "PUT",
        url: fieldSenseURL + "/account/" + id,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken,
        },
        crossDomain: false,
        cache: false,
        data: jsonData,
        dataType: 'json',
        success: function (data) {
            if (data.errorCode == '0000') {
                fieldSenseTosterSuccess(data.errorMessage, true);
                var table = $('#sample_5').DataTable();
                table.draw(false);
                $('#responsive2').modal('hide');
            } else {
                $('#pleaseWaitDialog').modal('hide');
                fieldSenseTosterError(data.errorMessage, true);
                //$('#responsive2').modal('hide');
                FieldSenseInvalidToken(data.errorMessage);
            }
            // added by jyoti 22-12-2016
            if (offlineAccountState !== allowOffline_edit) {

                var accountSettingsObject = {
                    "id": id,
                    "allowOffline": allowOffline_edit
                }

                var jsonData = JSON.stringify(accountSettingsObject);
                $.ajax({
                    type: "PUT",
                    url: fieldSenseURL + "/account/settings/values/" + id,
                    headers: {
                        "userToken": userToken
                    },
                    contentType: "application/json; charset=utf-8",
                    crossDomain: false,
                    data: jsonData,
                    cache: false,
                    dataType: 'json',
                    asyn: false,
                    success: function (data) {

                    }

                });

            }  //ended by jyoti
        }

    });


}

