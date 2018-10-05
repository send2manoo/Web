/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var intervalUploadPic = window.setInterval(function(){
    var userToken2=fieldSenseGetCookie("userToken");
    try{
        if(userToken2.toString()=="undefined"){
            window.location.href = "login.html";
        }else if(loginUserId!=0){
            loggedinUserImageData();
            loggedinUserData();
            UploadPicTemplate();
	    if(role==2){
		$('#top_tabs').html('<li class="inactive"><a href="Dashboard_accounts.html">Dashboard</a></li><li class="inactive"><a href="expenses_acc.html">Expenses</a></li>');
		$("#account_pages").attr("href","Dashboard_accounts.html");
	     }
            //            userPanelClick();
            window.clearTimeout(intervalUploadPic);
        }
    }
    catch(err){
        window.location.href = "login.html";
    }
},1000);
function UploadPicTemplate(){
    var actionURL=fieldSenseURL+"/user/profilePic/save/"+loginUserId+"/"+accountId;
    var UploadPicTemplateHtml='';
    UploadPicTemplateHtml+='<form id="fileupload" role="form" class="form-horizontal" action="'+actionURL+'" method="POST" enctype="multipart/form-data">';
    UploadPicTemplateHtml+='<div class="form-body">';
    UploadPicTemplateHtml+='<div class="form-group">';
    UploadPicTemplateHtml+='<label class="col-md-3 control-label">Photo Upload</label>';
    UploadPicTemplateHtml+='<div class="col-md-4">';
    UploadPicTemplateHtml+='<div class="fileupload fileupload-new" data-provides="fileupload">';
    UploadPicTemplateHtml+='<div class="fileupload-new thumbnail" style="width: 150px; height: 150px;">';
    UploadPicTemplateHtml+='<img src="'+imageURLPath+accountId+'_'+loginUserId+'_140X140.png?'+time+'" alt="" onerror="this.src=\'assets/img/noimage.jpg\';"/>';
    UploadPicTemplateHtml+='</div>';
    UploadPicTemplateHtml+='<div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 150px; max-height: 150px; line-height: 20px;">';
    UploadPicTemplateHtml+='</div>';
    UploadPicTemplateHtml+='<div>';
    UploadPicTemplateHtml+='<span class="btn btn-default btn-file">';
    UploadPicTemplateHtml+='<span class="fileupload-new">';
    UploadPicTemplateHtml+='<i class="fa fa-paperclip"></i> Select image';
    UploadPicTemplateHtml+='</span>';
    UploadPicTemplateHtml+='<span class="fileupload-exists">';
    UploadPicTemplateHtml+='<i class="fa fa-undo"></i> Change';
    UploadPicTemplateHtml+='</span>';
    UploadPicTemplateHtml+='<input type="file" class="default" id="id_file" name="fileUpload"/>';
    UploadPicTemplateHtml+='</span>';
    UploadPicTemplateHtml+='<a href="#" class="btn btn-danger fileupload-exists" data-dismiss="fileupload"><i class="fa fa-trash-o"></i> Remove</a>';
    UploadPicTemplateHtml+='</div>';
    UploadPicTemplateHtml+='</div>';
    UploadPicTemplateHtml+='</div>';
    UploadPicTemplateHtml+='</div>';
    UploadPicTemplateHtml+='</div>';
    UploadPicTemplateHtml+='<button type="button" class="btn btn-info" onClick="uploadProfilePic()">Save</button>';
    UploadPicTemplateHtml+='<button type="button" class="btn btn-default cls_btnspace" onClick="cancelProfilePic()">Cancel</button>';
    UploadPicTemplateHtml+='<iframe id="upload_target" name="upload_target" src="" style="width:0;height:0;border:0px solid #fff;" ></iframe>';
    UploadPicTemplateHtml+='</form>';
    $('#id_picupload').html(UploadPicTemplateHtml);
}
function uploadProfilePic(){
    document.getElementById('fileupload').target = 'upload_target';
    document.getElementById('fileupload').submit();
    setInterval(function(){
        if(userMode==1){
            window.location.href ="adminProfile.html";
        }else if((userMode==2)){
            window.location.href ="profile.html";
        }
    },10000);
}
function cancelProfilePic(){
    if(userMode==1){
        window.location.href ="adminProfile.html";
    }else if((userMode==2)){
        window.location.href ="profile.html";
    }
}

