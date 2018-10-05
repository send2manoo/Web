/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//added by nikhil
var loginUserIdcookie = fieldSenseGetCookie("loginUserIdcookie");
var loginUserNamecookie = fieldSenseGetCookie("loginUserNamecookie");
var accountIdcookie = fieldSenseGetCookie("accountIdcookie");
var accountNamecookie = fieldSenseGetCookie("accountNamecookie");
var UserRolecookie = fieldSenseGetCookie("UserRolecookie");
//ended by nikhil
var intervalIndex = window.setInterval(function () {
    var userToken2 = fieldSenseGetCookie("userToken");
    try {
        if (userToken2.toString() == "undefined") {
            window.location.href = "login.html";
        } else if (loginUserId != 0) {    // modified by manohar
              if (role == 1 || role==6 || role==3 || role==8) {       // role : 0 -super user, 1 -admin, 2 -account person, 5 -on-field user 
                loggedinUserImageData();
                loggedinUserData();
                organizationChartLoad();
            } else if (role == 0) {
                window.location.href = "stats.html";
            } else {
                window.location.href = "dashboard.html";
            }
            window.clearTimeout(intervalIndex);
        }
    }
    catch (err) {
        window.location.href = "login.html";
    }
}, 1000);
function organizationChartLoad() {
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/organizationChart",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                var organizationChartMembers = data.result;
                var options = new primitives.orgdiagram.Config();
                var items = new Array();
                var firstIteamId = 0;
                for (var i = 0; i < organizationChartMembers.length; i++) {
                    var id = organizationChartMembers[i].id;
                    var parentId = organizationChartMembers[i].parentId;
                    var userName = organizationChartMembers[i].user.firstName + " " + organizationChartMembers[i].user.lastName;
                    var designation = organizationChartMembers[i].user.designation;
                    var mobileNo = organizationChartMembers[i].user.mobileNo;
                    var emailId = organizationChartMembers[i].user.emailAddress;
                    var userRole = organizationChartMembers[i].user.role;
                    if (parentId == 0) {
                        parentId = null;
                        firstIteamId = id;
                    }
                    var item = new primitives.orgdiagram.ItemConfig({
                        id: id,
                        parent: parentId,
                        title: userName,
                        description: designation,
                        phone: mobileNo,
                        email: emailId,
                        role: userRole
                    })
                    items.push(item);
                }
                var buttons = [];
                buttons.push(new primitives.orgdiagram.ButtonConfig("edit", "fa fa-edit", "Edit"));
                buttons.push(new primitives.orgdiagram.ButtonConfig("add", "fa fa-plus", "Add"));
                buttons.push(new primitives.orgdiagram.ButtonConfig("delete", "fa fa-times", "Delete"));

                options.items = items;
                options.cursorItem = 0;
                options.buttons = buttons;
                options.hasButtons = primitives.common.Enabled.Auto;
                options.hasSelectorCheckbox = primitives.common.Enabled.False;
                options.leavesPlacementType = primitives.orgdiagram.ChildrenPlacementType.Matrix;
                options.onButtonClick = function (e, /*primitives.orgdiagram.EventArgs*/ data) {
                    switch (data.name) {
                        case "delete":
                            if (/*parentItem: primitives.orgdiagram.ItemConfig*/data.parentItem == null) {
                                fieldSenseTosterErrorNoTimeOut("Can't delete the top entry", true);
                            }
                            else {
                                deleteMemberFromOrgChart(data);
                            }
                            break;
                        case "add":
                            addMemberToChartTemplate(data.context.title, data.context.id, data.context.role);
                            break;
                        case "edit":
                            if (/*parentItem: primitives.orgdiagram.ItemConfig*/data.parentItem == null) {
                                fieldSenseTosterErrorNoTimeOut("Can't edit the top entry", true);
                            } else {
                                editMemberInChartTemplate(data.context.title, data.context.parent, data.context.id, data.context.role, data.parentItem.role);
                            }
                            break;
                    }
                };

                options.items = items;
                options.cursorItem = firstIteamId;
                options.templates = [getContactTemplate()];
                options.onItemRender = onTemplateRender;
                options.defaultTemplateName = "customControlTemplate";
                options.hasSelectorCheckbox = primitives.common.Enabled.False;
                jQuery("#basicdiagram2").orgDiagram(options);
                jQuery("#basicdiagram2").orgDiagram("update", /*Refresh: use fast refresh to update chart*/primitives.orgdiagram.UpdateMode.Refresh);
                $('#pleaseWaitDialog').modal('hide');
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}
/* organization chart functions start */
function getSubItemsForParent(items, parentItem) {
    var children = {},
            itemsById = {},
            index, len, item;
    for (index = 0, len = items.length; index < len; index += 1) {
        var item = items[index];
        if (children[item.parent] == null) {
            children[item.parent] = [];
        }
        children[item.parent].push(item);
    }
    var newChildren = children[parentItem.id];
    var result = {};
    if (newChildren != null) {
        while (newChildren.length > 0) {
            var tempChildren = [];
            for (var index = 0; index < newChildren.length; index++) {
                var item = newChildren[index];
                result[item.id] = item;
                if (children[item.id] != null) {
                    tempChildren = tempChildren.concat(children[item.id]);
                }
            }
            newChildren = tempChildren;
        }
    }
    return result;
}
function onTemplateRender(event, data) {
    var itemConfig = data.context;

    switch (data.renderingMode) {
        case primitives.common.RenderingMode.Create:
            /* Initialize widgets here */
            data.element.find("[name=shape]").bpShape({
                graphicsType: primitives.common.GraphicsType.SVG,
                lineType: primitives.common.LineType.Dashed,
                position: new primitives.common.Rect(0, 0, 172, 80),
                offset: -1,
                lineWidth: 1.2,
                fillColor: null,
                cornerRadius: 5
            });

            break;
        case primitives.common.RenderingMode.Update:
            /* Update widgets here */
            data.element.find("[name=shape]").bpShape("option", {
                shapeType: itemConfig["shapeType"]
            });
            data.element.find("[name=shape]").bpShape("update", primitives.common.UpdateMode.Refresh);
            break;
    }


    data.element.find("[name=photo]").attr({
        "src": itemConfig.image,
        "alt": itemConfig.title
    });
    data.element.find("[name=titleBackground]").css({
        "background": itemConfig.itemTitleColor
    });

    var fields = ["title", "description", "phone", "email"];
    for (var index = 0; index < fields.length; index++) {
        var field = fields[index];

        var element = data.element.find("[name=" + field + "]");
        if (element.text() != itemConfig[field]) {
            element.text(itemConfig[field]);
        }
    }
}
function getContactTemplate() {
    var result = new primitives.orgdiagram.TemplateConfig();
    result.name = "customControlTemplate";

    result.itemSize = new primitives.common.Size(174, 83);
    result.minimizedItemSize = new primitives.common.Size(3, 3);
    result.highlightPadding = new primitives.common.Thickness(2, 2, 2, 2);
    var itemTemplate = jQuery(
            '<div class="bp-item bp-corner-all bt-item-frame" style="border-width: 1px; width: 120px; height: 100px; top: 23px; left: 580.5px; position: absolute; overflow: hidden; padding: 0px; margin: 0px;">'
            + '<div name="titleBackground" class="bp-item bp-corner-all bp-title-frame" style="top: 2px; left: 2px; margin:0 auto; width: 170px; height: 18px; background-color: rgb(65, 105, 225); background-position: initial initial; background-repeat: initial initial;"><div name="title" class="bp-item bp-title" style="top: 1px; left: 4px; width: 108px; height: 16px; color: rgb(255, 255, 255);"></div></div>'
            + '<div name="description" class="bp-item" style="top: 62px; left: 6px; width: 120px; height: 18px; font-size: 10px; text-align:left;"></div>'
            + '<div name="phone" class="bp-item" style="top: 26px; left: 6px; width: 120px; height: 18px; font-size: 12px; text-align:left;"></div>'
            + '<div name="email" class="bp-item" style="top: 44px; left: 6px; width: 120px; height: 18px; font-size: 12px; text-align:left;"></div>'
            + '</div>'
            ).css({
        width: result.itemSize.width + "px",
        height: result.itemSize.height + "px"
    }).addClass("bp-item bp-corner-all bt-item-frame");
    result.itemTemplate = itemTemplate.wrap('<div>').parent().html();

    return result;
}
/* organization chart functions end */
function addMemberToChartTemplate(parentName, parentId, parentRole) {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/organizationChart/membersToChart",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            $('.cls_addMemberToChart').html('');
            $(".cls_addMemberToChart").modal('show');
            var chartMembers = data.result;
            var addMemberToChartTemplateHtml = '';
            addMemberToChartTemplateHtml += '<div class="modal-dialog modal-wide">';
            addMemberToChartTemplateHtml += '<div class="modal-content">';
            addMemberToChartTemplateHtml += '<div class="modal-header">';
            addMemberToChartTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
            addMemberToChartTemplateHtml += '<h4 class="modal-title">Add Subordinate</h4>';
            addMemberToChartTemplateHtml += '</div>';
            addMemberToChartTemplateHtml += '<form class="form-horizontal" role="form">';
            addMemberToChartTemplateHtml += '<div class="modal-body">';
            addMemberToChartTemplateHtml += '<div class="form-body">';
            addMemberToChartTemplateHtml += '<div class="form-group">';
            addMemberToChartTemplateHtml += '<label class="col-md-5 control-label">Reports To</label>';
            addMemberToChartTemplateHtml += '<div class="col-md-7">';
            addMemberToChartTemplateHtml += '<input type="fname" class="form-control" value="' + parentName + '" disabled>';
            addMemberToChartTemplateHtml += '</div>';
            addMemberToChartTemplateHtml += '</div>';
            addMemberToChartTemplateHtml += '<div class="form-group">';
            addMemberToChartTemplateHtml += '<label class="col-md-5 control-label">Select User</label>';
            addMemberToChartTemplateHtml += '<div class="col-md-7">';
            addMemberToChartTemplateHtml += '<select class="form-control" id="id_subordinate" tabindex="2">';
            addMemberToChartTemplateHtml += '<option value="0">Select</option>';
            var userRole;
            for (var i = 0; i < chartMembers.length; i++) {
                userRole = chartMembers[i].role;
                if (parentId != 100000 && parentRole != 1 && parentRole != userRole) {
                    continue;
                }
                var userName = chartMembers[i].firstName + " " + chartMembers[i].lastName;
                var userId = chartMembers[i].id;
                addMemberToChartTemplateHtml += '<option value="' + userId + '">' + userName + '</option>';
            }
            addMemberToChartTemplateHtml += '</select>';
            addMemberToChartTemplateHtml += '</div>';
            addMemberToChartTemplateHtml += '</div>';
            addMemberToChartTemplateHtml += '</div>';
            addMemberToChartTemplateHtml += '</div>';
            addMemberToChartTemplateHtml += '<div class="modal-footer">';
            addMemberToChartTemplateHtml += '<button type="button" class="btn btn-info cls_addMemToChart" onClick="saveMemberToChart(' + parentId + ');return;">Save</button>';
            addMemberToChartTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
            addMemberToChartTemplateHtml += '</div>';
            addMemberToChartTemplateHtml += '</form>';
            addMemberToChartTemplateHtml += '</div>';
            addMemberToChartTemplateHtml += '</div>';
            $('.cls_addMemberToChart').html(addMemberToChartTemplateHtml);
        }
    });
}
function saveMemberToChart(parentId) {
    $('.cls_addMemToChart').prop('disabled', true);
    var indexOfownerId = document.getElementById("id_subordinate");
    var ownerId = indexOfownerId.options[indexOfownerId.selectedIndex].value;
    if (ownerId == 0) {
        fieldSenseTosterError("Please select the member .", true);
        $('.cls_addMemToChart').prop('disabled', false);
        return false;
    }
    var teamObject = {
        "teamName": "Hirarchy",
        "description": "Hirarchy",
        "ownerId": {
            "id": ownerId
        },
        "isActive": 1,
        "createdBy": {
            "id": loginUserId
        }
    }
    var jsonData = JSON.stringify(teamObject);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/team/organizationChart/member/add/" + parentId,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                clevertap.event.push("Add Node", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                var userData = data.result;
                var userName = userData.user.firstName + " " + userData.user.lastName;
                var designation = userData.user.designation;
                var mobileNo = userData.user.mobileNo;
                var emailId = userData.user.emailAddress;
                var userRole = userData.user.role;
                var chiledId = userData.id;
                parentId = userData.parentId;
                $(".cls_addMemberToChart").modal('hide');
                /* get items collection */
                var items = jQuery("#basicdiagram2").orgDiagram("option", "items");
                /* create new item */
                var newItem = new primitives.orgdiagram.ItemConfig({
                    id: chiledId,
                    parent: parentId,
                    title: userName,
                    description: designation,
                    phone: mobileNo,
                    email: emailId,
                    role: userRole
                });
                /* add it to items collection and put it back to chart, actually it is the same reference*/
                items.push(newItem);
                jQuery("#basicdiagram2").orgDiagram({
                    items: items,
                    cursorItem: newItem.id
                });
                /* updating chart options does not fire its referesh, so it should be call explicitly */
                jQuery("#basicdiagram2").orgDiagram("update", /*Refresh: use fast refresh to update chart*/ primitives.orgdiagram.UpdateMode.Refresh);
                $('#pleaseWaitDialog').modal('hide');
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}
function editMemberInChartTemplate(parentName, parentId, id, role, parentRole) {
    $.ajax({
        type: "GET",
        url: fieldSenseURL + "/team/organizationChart/member/get/" + id,
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (orgChartMemberDataEditData) {
            var orgChartMemberEditUserId = orgChartMemberDataEditData.result.id;
            $.ajax({
                type: "GET",
                url: fieldSenseURL + "/team/organizationChart/member/get/" + parentId,
                contentType: "application/json; charset=utf-8",
                headers: {
                    "userToken": userToken
                },
                crossDomain: false,
                cache: false,
                dataType: 'json',
                asyn: true,
                success: function (orgChartMemberData) {
                    parentName = orgChartMemberData.result.firstName + " " + orgChartMemberData.result.lastName;
                    $.ajax({
                        type: "GET",
                        url: fieldSenseURL + "/team/organizationChart/membersToChart",
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        asyn: true,
                        success: function (data) {
                            $('.cls_addMemberToChart').html('');
                            $(".cls_addMemberToChart").modal('show');
                            var chartMembers = data.result;
                            var editMemberInChartTemplateHtml = '';
                            editMemberInChartTemplateHtml += '<div class="modal-dialog modal-wide">';
                            editMemberInChartTemplateHtml += '<div class="modal-content">';
                            editMemberInChartTemplateHtml += '<div class="modal-header">';
                            editMemberInChartTemplateHtml += '<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>';
                            editMemberInChartTemplateHtml += '<h4 class="modal-title"> Edit</h4>';
                            editMemberInChartTemplateHtml += '</div>';
                            editMemberInChartTemplateHtml += '<form class="form-horizontal" role="form">';
                            editMemberInChartTemplateHtml += '<div class="modal-body">';
                            editMemberInChartTemplateHtml += '<div class="form-body">';
                            editMemberInChartTemplateHtml += '<div class="form-group">';
                            editMemberInChartTemplateHtml += '<label class="col-md-5 control-label">Reports To</label>';
                            editMemberInChartTemplateHtml += '<div class="col-md-7">';
                            editMemberInChartTemplateHtml += '<input type="fname" class="form-control" value="' + parentName + '" disabled>';
                            editMemberInChartTemplateHtml += '</div>';
                            editMemberInChartTemplateHtml += '</div>';
                            editMemberInChartTemplateHtml += '<div class="form-group">';
                            editMemberInChartTemplateHtml += '<label class="col-md-5 control-label">Select User</label>';
                            editMemberInChartTemplateHtml += '<div class="col-md-7">';
                            editMemberInChartTemplateHtml += '<select class="form-control" id="id_subordinate" tabindex="2">';
                            editMemberInChartTemplateHtml += '<option value="0">Select</option>';
                            var userRole;
                            for (var i = 0; i < chartMembers.length; i++) {
                                userRole = chartMembers[i].role;
                                if ((userRole != 1 && userRole != role) || (parentId != 100000 && parentRole != 1 && parentRole != userRole)) {
                                    continue;
                                }
                                var userName = chartMembers[i].firstName + " " + chartMembers[i].lastName;
                                var userId = chartMembers[i].id;
                                editMemberInChartTemplateHtml += '<option value="' + userId + '">' + userName + '</option>';
                            }
                            editMemberInChartTemplateHtml += '</select>';
                            editMemberInChartTemplateHtml += '</div>';
                            editMemberInChartTemplateHtml += '</div>';
                            editMemberInChartTemplateHtml += '</div>';
                            editMemberInChartTemplateHtml += '</div>';
                            editMemberInChartTemplateHtml += '<div class="modal-footer">';
                            editMemberInChartTemplateHtml += '<button type="button" class="btn btn-info" onClick="editMemberInOrgChart(\'' + parentId + '\',\'' + id + '\',\'' + orgChartMemberEditUserId + '\');return;">Save</button>';
                            editMemberInChartTemplateHtml += '<button type="button" data-dismiss="modal" class="btn btn-default">Cancel</button>';
                            editMemberInChartTemplateHtml += '</div>';
                            editMemberInChartTemplateHtml += '</form>';
                            editMemberInChartTemplateHtml += '</div>';
                            editMemberInChartTemplateHtml += '</div>';
                            $('.cls_addMemberToChart').html(editMemberInChartTemplateHtml);
                        }
                    });
                }
            });
        }
    });
}
function editMemberInOrgChart(parentId, chaildId, editUserId) {
    var indexOfMemberId = document.getElementById("id_subordinate");
    var memberId = indexOfMemberId.options[indexOfMemberId.selectedIndex].value;
    if (memberId == 0) {
        fieldSenseTosterError("Please select the member .", true);
        return false;
    }
    var orgMemberObj = {
        "id": chaildId,
        "parentId": parentId,
        "user": {
            "id": memberId,
            "type": editUserId
        }
    }
    var jsonData = JSON.stringify(orgMemberObj);
    $('#pleaseWaitDialog').modal('show');
    $.ajax({
        type: "POST",
        url: fieldSenseURL + "/team/organizationChart/member/update",
        contentType: "application/json; charset=utf-8",
        headers: {
            "userToken": userToken
        },
        crossDomain: false,
        data: jsonData,
        cache: false,
        dataType: 'json',
        asyn: true,
        success: function (data) {
            if (data.errorCode == '0000') {
                clevertap.event.push("Edit Node", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                    "Source": "Web",
                    "Account name": accountNamecookie,
                    "UserRolecookie": UserRolecookie,
                });
                $(".cls_addMemberToChart").modal('hide');
                organizationChartLoad();
                $('#pleaseWaitDialog').modal('hide');
            } else {
                fieldSenseTosterError(data.errorMessage, true);
                $('#pleaseWaitDialog').modal('hide');
            }
        }
    });
}
function deleteMemberFromOrgChart(data) {
    var deletedPositionId = data.context.id;
    var email = data.context.email;
    var name = data.context.title;
    var parent = data.context.parent;
    //var deleteConfirm = confirm("Are you sure, you want to remove "+name+" from the Organization Chart? ");
    // if(deleteConfirm){
    bootbox.dialog({
        message: "Are you sure you want to remove " + name + " from the Organization Chart ?",
        title: "Remove " + name,
        buttons: {
            yes: {
                label: "Yes",
                className: "btn-default",
                callback: function () {
                    $('#pleaseWaitDialog').modal('show');
                    $.ajax({
                        type: "DELETE",
                        url: fieldSenseURL + "/team/orgChart/" + email + "/" + deletedPositionId + "/" + parent,
                        contentType: "application/json; charset=utf-8",
                        headers: {
                            "userToken": userToken
                        },
                        crossDomain: false,
                        cache: false,
                        dataType: 'json',
                        asyn: true,
                        success: function (data2) {
                            if (data2.errorCode == '0000') {
                                clevertap.event.push("Delete Node", {
//                    "UserId": loginUserIdcookie,
//                    "User Name": loginUserNamecookie,
//                    "AccountId": accountIdcookie,
                                    "Source": "Web",
                                    "Account name": accountNamecookie,
                                    "UserRolecookie": UserRolecookie,
                                });

                                var items = jQuery("#basicdiagram2").orgDiagram("option", "items");
                                var newItems = [];
                                /* collect all children of deleted items, we are going to delete them as well. */
                                var itemsToBeDeleted = getSubItemsForParent(items, /*context: primitives.orgdiagram.ItemConfig*/data.context);
                                /* add deleted item to that collection*/
                                itemsToBeDeleted[data.context.id] = true;

                                /* copy to newItems collection only remaining items */
                                for (var index = 0, len = items.length; index < len; index += 1) {
                                    var item = items[index];
                                    if (!itemsToBeDeleted.hasOwnProperty(item.id)) {
                                        newItems.push(item);
                                    }
                                }
                                /* update items list in chart */
                                jQuery("#basicdiagram2").orgDiagram({
                                    items: newItems,
                                    cursorItem: data.parentItem.id
                                });
                                jQuery("#basicdiagram2").orgDiagram("update", /*Refresh: use fast refresh to update chart*/ primitives.orgdiagram.UpdateMode.Refresh);
                                organizationChartLoad();
                                $('#pleaseWaitDialog').modal('hide');
                            } else {
                                fieldSenseTosterError(data.errorMessage, true);
                                $('#pleaseWaitDialog').modal('hide');
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            $('#pleaseWaitDialog').modal('hide');
                            alert("in error:" + thrownError);
                        }
                    });
                }
            },
            no: {
                label: "No",
                className: "btn-default",
                callback: function () {
                    //$('#pleaseWaitDialog').modal('hide');
                    //return false;
                }
            }
        }
    });

    // }
}
