/*-----Duplicate-ID's-----*/
    var allElements = document.getElementsByTagName("*"), allIds = {}, dupIDs = [];
    for (var i = 0, n = allElements.length; i < n; ++i) {
    var el = allElements[i];
    if (el.id) {
        if (allIds[el.id] !== undefined) dupIDs.push(el.id);
        allIds[el.id] = el.name || el.id;
        }
    }
    if (dupIDs.length) { console.error("Duplicate ID's:", dupIDs);} else { console.log("No Duplicates Detected"); }  
/*--------------*/

var page_history = ["raise_a_case"];
var search_list_user = [];
var hide_tooltip_timeOut;
var my_cases_tab_headers;

var aVol = "<li> <span class=\"vol_tooltip\">";
var bVol = "</span> <button onclick=\"assignCase(this, '')\" type=\"button\" class=\"assign-case-assign-btn\">Assign</button>";
var cVol = "<input type=\"hidden\" id=\"vol_id\" value=\"";
var dVol = "\" ></li>";

var top_nav_a_Vol = "<li> <span class=\"vol_tooltip\">";
var top_nav_b_Vol = "</span> <button onclick=\"assignCase(this, 'top-nav')\" type=\"button\" class=\"assign-case-assign-btn\">Assign</button>";
var top_nav_c_Vol = "<input type=\"hidden\" id=\"vol_id\" value=\"";
var top_nav_d_Vol = "\" ></li>";

var aCase = '<div class ="row row1" onclick="case_details(this)"> <div class="sr-no"><li></li></div> <div class="case-id">';
var bCase = '</div> <div class="case-status">';
var b1Case = '</div> <div class="accepted">';
var cCase = '</div> <div class="date">';
var dCase = '</div> <div class="animal-type">';
var eCase = '</div> <div class="case-name">';
var fCase = '</div> </div>';

var a_1Case = '<div class ="row row1" onclick="top_nav_user_cases(this)"> <div class="sr-no"><li></li></div> <div class="case-id">';
var b_1Case = '</div> <div class="case-status">';
var b1_1Case = '</div> <div class="accepted">';
var c_1Case = '</div> <div class="date">';
var d_1Case = '</div> <div class="animal-type">';
var e_1Case = '</div> <div class="case-name">';
var f_1Case = '</div> </div>';

var caseIdVsInfoMap = {};
var userIdVsInfoMap = {};
var currentCaseId = -1;
var responseHandler = function (event) {
    var data = event.data.split(":");
    var url = data[0];
    var tableId = data[1];
    var userId = -1;
    if (data.length == 3) {
        userId = data[2];
    }
    //-----Loading-Screens---------------------------------------------
    if($("#table11_tab_content_loading").length === 0) $("#table11").append("<img src='/img/loading-simple.gif' id='table11_tab_content_loading' alt='Loading'>");
    if($("#table22_tab_content_loading").length === 0) $("#table22").append("<img src='/img/loading-simple.gif' id='table22_tab_content_loading' alt='Loading'>");
    if($("#table33_tab_content_loading").length === 0) $("#table33").append("<img src='/img/loading-simple.gif' id='table33_tab_content_loading' alt='Loading'>");
    if($("#table1_tab_content_loading").length === 0) $("#table1").append("<img src='/img/loading-simple.gif' id='table1_tab_content_loading' alt='Loading'>");
    if($("#table2_tab_content_loading").length === 0) $("#table2").append("<img src='/img/loading-simple.gif' id='table2_tab_content_loading' alt='Loading'>");
    if($("#table3_tab_content_loading").length === 0) $("#table3").append("<img src='/img/loading-simple.gif' id='table3_tab_content_loading' alt='Loading'>");
    //----------------------------------------------------------------
    var formData = {
        'userId': userId,
    };
    console.info(" Clicked " + url + " " + tableId + " for user : " + userId);
    $.ajax({
        type: 'GET',
        data: formData,
        url: url,
        dataType: 'json'
    })
        .done(function (data) {
                var cc = [];
                if(data === null || data === undefined || data.length === 0){
                    $('#' + tableId).empty();
                    $('#' + tableId).html("<div style='margin-top:5vh;color:#007AEA;font-size:1.5em;'>No Cases</div>");
                }
                else {
                    $("#" + tableId + "_tab_content_loading").css("display","none");
                    $('#' + tableId).empty();
                    if(tableId === "table1" || tableId === "table2" || tableId === "table3"){
                        $.each(data, function (i, item) {
                            caseIdVsInfoMap[item.caseId] = item;
                            var htm = aCase + item.caseId + bCase + (item.userNameCurrent ? item.userNameCurrent+"("+item.userRoleCurrent+")" : "Closed" )  + b1Case+(item.isAck == 1 ? "Yes" : item.isAck == -1 ? "No" : "Pending" ) +cCase + item.creationDateStr + dCase + item.typeAnimal + eCase + item.animalName + fCase;
                            cc.push(htm);
                            $('#' + tableId).html(cc.join(""));
                        });
                    }
                    else {
                        $.each(data, function (i, item) {
                            caseIdVsInfoMap[item.caseId] = item;
                            var htm = a_1Case + item.caseId + b_1Case + (item.userNameCurrent ? item.userNameCurrent+"("+item.userRoleCurrent+")" : "Closed" )  + b1_1Case+(item.isAck == 1 ? "Yes" : item.isAck == -1 ? "No" : "Pending" ) +c_1Case + item.creationDateStr + d_1Case + item.typeAnimal + e_1Case + item.animalName + f_1Case;
                            cc.push(htm);
                            $('#' + tableId).html(cc.join(""));
                        });
                    }
                }
            }
        );
};

function layer_change(layer){
    if (layer === "layer1"){
        $('#heading-status_bar').css("display","block");
        $('#top-nav-heading-status_bar').css("display","none");
    }
    else if(layer === "layer2"){
        $('#heading-status_bar').css("display","none");
        $('#top-nav-heading-status_bar').css("display","block");
    }
}

function top_nav_user_cases(data) {
    $("#user-profile").hide();
    $("#top-nav-case-details").show();
    $("#top-nav-case-details-form").css("display","flex");
    page_history.unshift("vol_cases_details");
    currentCaseId = $(data).find(".case-id").text();
    var caseInfo = caseIdVsInfoMap[currentCaseId];
    $('#top-nav-case-id-case-details').val(caseInfo.caseId);
    $('#top-nav-animal-type-case-details').val(caseInfo.typeAnimal);
    $('#top-nav-animal-name-case-details').val(caseInfo.animalName);
    $('#top-nav-condition-case-details').val(caseInfo.animalCondition);
    $('#top-nav-contact-name-case-details').val(caseInfo.contactName);
    $('#top-nav-nine-one-case-details').val(caseInfo.contactPrefix);
    $('#top-nav-contact-number-case-details').val(caseInfo.contactNumber);
    $('#top-nav-location-case-details').val(caseInfo.location);
    $('#top-nav-location-landmark-case-details').val(caseInfo.locationLandMark);
    $('#top-nav-location-pincode-case-details').val(caseInfo.locationPincode);
    layer_change('layer2');
    $("#top-nav-heading-text").html("Case Details | Case No.: " + caseInfo.caseId);
    $("#top-nav-back-close-btn-wrapper").css("display","inline");
    caseImageRetriever(caseInfo.caseId,"top-nav-case-photos-case-details");
    /*if(!caseInfo.active) {
        $('#action-center-assign-case').hide();
        $('#action-center-close-case').hide();
    }
    else {
        $('#action-center-assign-case').show();
        $('#action-center-close-case').show();
    }*/
    if(caseInfo.birdOrAnimal === "Animal"){
        $("#header-form-left-side-case-details").html("Animal Information");
        $("#animal-bird-type-case-details").html("Animal Type: ");
        $("#animal-bird-name-case-details").html("Animal Name: ");
    } else {
        $("#header-form-left-side-case-details").html("Bird Information");
        $("#animal-bird-type-case-details").html("Bird Type: ");
        $("#animal-bird-name-case-details").html("Bird Name: ");
    }
}

function case_details(data) {
    document.getElementById('my-cases-content').style.display = "none";
    document.getElementById('case-details').style.display = "block";
    document.getElementById('case-details-form').style.display = "flex";
    document.getElementById('action-center').style.display = "block";
    page_history.unshift("myc_case_details");
    currentCaseId = $(data).find(".case-id").text();
    var caseInfo = caseIdVsInfoMap[currentCaseId];
    $('#case-id-case-details').val(caseInfo.caseId);
    $('#animal-type-case-details').val(caseInfo.typeAnimal);
    $('#animal-name-case-details').val(caseInfo.animalName);
    $('#condition-case-details').val(caseInfo.animalCondition);
    $('#contact-name-case-details').val(caseInfo.contactName);
    $('#nine-one-case-details').val(caseInfo.contactPrefix);
    $('#contact-number-case-details').val(caseInfo.contactNumber);
    $('#location-case-details').val(caseInfo.location);
    $('#location-landmark-case-details').val(caseInfo.locationLandMark);
    $('#location-pincode-case-details').val(caseInfo.locationPincode);
    layer_change('layer1');
    $("#heading-text").html("Case Details | Case No.: " + caseInfo.caseId);
    $("#back-close-btn-wrapper").css("display","inline");
    $("#myc_case_details_loading_screen").css("display","block");
    caseImageRetriever(caseInfo.caseId,"case-photos-case-details");
    if(!caseInfo.active) {
        $('#action-center-assign-case').hide();
        $('#action-center-close-case').hide();
    }
    else {
        $('#action-center-assign-case').show();
        $('#action-center-close-case').show();
    }
    if(caseInfo.birdOrAnimal === "Animal"){
        $("#header-form-left-side-case-details").html("Animal Information");
        $("#animal-bird-type-case-details").html("Animal Type: ");
        $("#animal-bird-name-case-details").html("Animal Name: ");
    } else {
        $("#header-form-left-side-case-details").html("Bird Information");
        $("#animal-bird-type-case-details").html("Bird Type: ");
        $("#animal-bird-name-case-details").html("Bird Name: ");        
    }
}

function raise_a_case() {
    layer_change('layer1');
    $('#heading-text').html("Raise a Case");
    $("#back-close-btn-wrapper").css("display","none");
    document.getElementById('rac-body').classList.add('right-side-card-rotate');
    document.getElementById('rac-body').style.borderBottom = "5px solid #007AEA";
    document.getElementById('myc-body').style.borderBottom = "none";
    document.getElementById('usdca-body').style.borderBottom = "none";
    setTimeout(function () {
        document.getElementById('rac-body').classList.remove('right-side-card-rotate');
    }, 600);
    page_history.unshift("raise_a_case");
    document.getElementById('raise-a-case-content').style.display = "block";
    document.getElementById('my-cases-content').style.display = "none";
    document.getElementById('user-data-change-approvals-content').style.display = "none";
    document.getElementById('top-nav-search-results').style.display = "none";
    document.getElementById("top-nav-search").value = "";
    document.getElementById("user-profile").style.display = "none";
    document.getElementById("case_profile").style.display = "none";
    document.getElementById('case-details').style.display = "none";
    document.getElementById("table4").style.display = "none";
    document.getElementById('top-nav-case-details').style.display = "none";
    document.getElementById('top-nav-case-details-form').style.display = "none";
}

function my_cases() {
    layer_change('layer1');
    $('#heading-text').html("My Cases");
    $("#back-close-btn-wrapper").css("display","none");
    document.getElementById('myc-body').classList.add('right-side-card-rotate');
    document.getElementById('myc-body').style.borderBottom = "5px solid #007AEA";
    document.getElementById('rac-body').style.borderBottom = "none";
    document.getElementById('usdca-body').style.borderBottom = "none";
    setTimeout(function () {
        document.getElementById('myc-body').classList.remove('right-side-card-rotate');
    }, 600);
    page_history.unshift("my_cases");
    $("#active-tab-header").click();
    document.getElementById('active-tab-header').classList.add('active-header');
    document.getElementById('raise-a-case-content').style.display = "none";
    document.getElementById('my-cases-content').style.display = "block";
    document.getElementById('search-case-input').value = "";
    document.getElementById('user-data-change-approvals-content').style.display = "none";
    $('#tab-header').fadeIn(300);
    showresults();
    document.getElementById("table4").style.display = "none";
    document.getElementById('top-nav-search-results').style.display = "none";
    document.getElementById("user-profile").style.display = "none";
    document.getElementById("case_profile").style.display = "none";
    document.getElementById('case-details').style.display = "none";
    document.getElementById('content-assign-case').style.display = "none";
    document.getElementById('case-details-form').style.opacity = "1";
    document.getElementById('content-close-case').style.display = "none";
    document.getElementById('case-details-form').style.pointerEvents = "";
    document.getElementById('top-nav-case-details').style.display = "none";
    document.getElementById('top-nav-case-details-form').style.display = "none";
}

function user_data_change_approvals() {
    layer_change('layer1');
    $('#heading-text').html("User Data Change Approvals");
    $("#back-close-btn-wrapper").css("display","none");
    document.getElementById('usdca-body').classList.add('right-side-card-rotate');
    document.getElementById('usdca-body').style.borderBottom = "5px solid #007AEA";
    document.getElementById('rac-body').style.borderBottom = "none";
    document.getElementById('myc-body').style.borderBottom = "none";
    setTimeout(function () {
        document.getElementById('usdca-body').classList.remove('right-side-card-rotate');
    }, 600);
    page_history.unshift("user_data_change_approvals");
    document.getElementById('raise-a-case-content').style.display = "none";
    document.getElementById('my-cases-content').style.display = "none";
    document.getElementById('user-data-change-approvals-content').style.display = "block";
    document.getElementById('top-nav-search-results').style.display = "none";
    document.getElementById("top-nav-search").value = "";
    document.getElementById("user-profile").style.display = "none";
    document.getElementById("case_profile").style.display = "none";
    document.getElementById('case-details').style.display = "none";
    document.getElementById("table4").style.display = "none";
    document.getElementById('top-nav-case-details').style.display = "none";
    document.getElementById('top-nav-case-details-form').style.display = "none";
}

function top_nav_search_user_profile() {
    //$("#top-nav-search-results").css("display","none");
    $("#top-nav-case-details").css("display","none");
    $("#user-profile").css("display","block");
    $("#user-profile-cases-content").css("display","none");
    page_history.unshift("top_nav_search_user_profile");
    $("#user-profile-about-content").fadeIn();
    user_profile_basic_details();
}

function top_nav_search_case_details(identifier1) {
    document.getElementById('top-nav-case-details').style.display = "block";
    document.getElementById('top-nav-case-details-form').style.display = "flex";
    document.getElementById('top-nav-search-results').style.display = "none";
    document.getElementById("user-profile").style.display = "none";
    document.getElementById("case_profile").style.display = "none";
    layer_change("layer2");
    $("#top-nav-heading-text").html("Case Details | Case No.: " + " ");
    $("#top-nav-back-close-btn-wrapper").css("display","block");
    if (identifier1 === "vol_cases_details") {
        page_history.unshift("vol_cases_details");
    } else if (identifier1 === "search_cases_details") {
        page_history.unshift("search_cases_details");
    }
}

function user_profile_basic_details() {
    document.getElementById("user-profile-basic-details-btn").style.border = "2px solid #007AEA";
    document.getElementById("user-profile-user-cases-btn").style.border = "2px solid transparent";
    $("#user-profile-cases-content").fadeOut(200);
    var user_profile_about_conent = setTimeout(function () {
        $("#user-profile-about-content").fadeIn();
    }, 210);
}

function user_profile_user_cases(){
    $("#user-profile-active-tab-header").click();
    document.getElementById('user-profile-active-tab-header').classList.add('active-header');
    document.getElementById("user-profile-user-cases-btn").style.border = "2px solid #007AEA";
    document.getElementById("user-profile-basic-details-btn").style.border = "2px solid transparent";
    $("#user-profile-about-content").fadeOut(200);
    var user_profile_user_cases_timeoOut = setTimeout(function () {
        $("#user-profile-cases-content").fadeIn();
    }, 210);
}

function hideresults() {
    if(document.getElementById("active-tab-content").style.display === "block"){
        my_cases_tab_headers = "active-tab-content";
    } else if (document.getElementById("recent-tab-content").style.display === "block"){
        my_cases_tab_headers = "recent-tab-content";
    } else if (document.getElementById("closed-tab-content").style.display === "block"){
        my_cases_tab_headers = "closed-tab-content";
    }
    document.getElementById(my_cases_tab_headers).style.display = "none";
}

function showresults() {
    $('#table4').fadeOut(300);
    $('#' + my_cases_tab_headers).fadeIn(300);
}

function assignCase(data, param) {
    var userId = $(data).parent().find("#vol_id").val();
    assignCaseReq(currentCaseId, userId, param);
}

function other_reason_close_case(param) {
    if (document.getElementById(param+'close-case-reason').value === "Other") {
        document.getElementById(param+'close-case-other-reason').style.display = "block";
        document.getElementById(param+'close-case-other-reason').value = "";
        document.getElementById(param+'close-case-other-reason').innerHTML = "";
        document.getElementById(param+'close-case-other-reason').style.border = "1px solid #666";
        document.getElementById(param+'close-case-reason').style.border = "1px solid #666";
        document.getElementById(param+'close-case-other-reason').placeholder = "Specify Reason";
    } else {
        document.getElementById(param+'close-case-other-reason').style.display = "none";
        document.getElementById(param+'close-case-reason').style.border = "1px solid #666";
    }
}

function navigate_back() {
    if (page_history[0] === "myc_case_details") {
        document.getElementById('my-cases-content').style.display = "block";
        document.getElementById('case-details').style.display = "none";
        document.getElementById('case-details-form').style.display = "none";
        layer_change('layer1');
        $("#heading-text").html("My Cases");
        $("#back-close-btn-wrapper").css("display","none");
        var loading = "<img src='/img/loading-simple.gif' id='myc_case_details_loading_screen' alt='Loading'>";
        $("#case-photos-case-details").empty();
        $("#case-photos-case-details").append(loading);
        $("#myc_case_details_loading_screen").css("display","block");
        if($("#active-tab-header").hasClass("tab-header-animation")) $("#active-tab-header").click();
        if($("#recent-tab-header").hasClass("tab-header-animation")) $("#recent-tab-header").click();
        if($("#closed-tab-header").hasClass("tab-header-animation")) $("#closed-tab-header").click();
        page_history.unshift("my_cases");
    } else if (page_history[0] === "action_centre_assign_case" || page_history[0] === "action_centre_close_case") {
        document.getElementById('case-details-form').style.display = "flex";
        document.getElementById('case-details-form').style.pointerEvents = "";
        document.getElementById('case-details-form').style.opacity = "1";
        document.getElementById('content-assign-case').style.display = "none";
        document.getElementById('content-close-case').style.display = "none";
        document.getElementById('assign-case-success').style.display = "none";
        document.getElementById('assign-case-error').style.display = "none";
        $("#content-assign-case").css("pointerEvents", "");
        document.getElementById('top-nav-search-results').style.display = "none";
        let caseId = $("#case-id-case-details").val();
        layer_change('layer1');
        $("#heading-text").html("Case Details | Case No.: " + caseId);
        page_history.unshift("myc_case_details");
    }/*
    else if (page_history[0] === "user_cases_case_details"){
        document.getElementById('top-nav-case-details').style.display = "none";
        document.getElementById('heading-top-nav-case-details').style.display = "none";
        document.getElementById('top-nav-case-details-form').style.display = "none";
        document.getElementById('top-nav-search-heading').style.display = "block";
        document.getElementById("top-nav-search-heading-content").innerHTML = "#Search_Input - #User_Name";
        document.getElementById("top-nav-search-user-profile-back-button").style.display = "block";
        document.getElementById("user-profile").style.display = "block";
    }*/
    else if (page_history[0] === "top_nav_search_user_profile") {
        close();
        /*document.getElementById('top-nav-search-results').style.display = "block";
        document.getElementById("user-profile").style.display = "none";
        document.getElementById("case_profile").style.display = "none";
        page_history.unshift("top_nav_search_results");*/
    }/* else if (page_history[0] === "vol_cases_details") {
        document.getElementById('top-nav-case-details').style.display = "none";
        document.getElementById('heading-top-nav-case-details').style.display = "none";
        document.getElementById('top-nav-case-details-form').style.display = "none";
        document.getElementById('top-nav-search-heading').style.display = "block";
        document.getElementById("user-profile").style.display = "block";
        page_history.unshift("top_nav_search_user_profile");
    } else if (page_history[0] === "search_cases_details") {
        document.getElementById('top-nav-search-heading').style.display = "block";
        document.getElementById('top-nav-search-results').style.display = "block";
        document.getElementById('top-nav-case-details').style.display = "none";
        document.getElementById('heading-top-nav-case-details').style.display = "none";
        document.getElementById('top-nav-case-details-form').style.display = "none";
        document.getElementById("top-nav-search-user-profile-back-button").style.display = "none";
        page_history.unshift("top_nav_search_results");
    }*/ 
    else if (page_history[0] === "vol_cases_details") {
        document.getElementById('top-nav-case-details').style.display = "none";
        document.getElementById('top-nav-case-details-form').style.display = "none";
        document.getElementById("user-profile").style.display = "block";
        layer_change('layer2');
        $("#top-nav-heading-text").html("Search User | " + search_list_user[0]);
        page_history.unshift("top_nav_search_user_profile");
    } else if (page_history[0] === "search_cases_details") {
        close();
    }else if (page_history[0] === "top-nav-action_centre_assign_case" || page_history[0] === "top-nav-action_centre_close_case") {
        document.getElementById('top-nav-case-details-form').style.display = "flex";
        document.getElementById('top-nav-case-details-form').style.opacity = "1";
        document.getElementById('top-nav-case-details-form').style.pointerEvents = "";
        document.getElementById('top-nav-content-assign-case').style.display = "none";
        document.getElementById('top-nav-content-close-case').style.display = "none";
        document.getElementById('top-nav-assign-case-success').style.display = "none";
        document.getElementById('top-nav-assign-case-error').style.display = "none";
        $("#top-nav-content-assign-case").css("pointerEvents", "");
        document.getElementById('top-nav-search-results').style.display = "none";
        let caseId = $("#top-nav-case-id-case-details").val();
        $("#top-nav-heading-text").html("Case Details | Case No.: " + caseId);
        for(let i = 0; i < page_history.length; i++){
            if(page_history[i] === "search_cases_details"){
                page_history.unshift("search_cases_details");
                break;
            }else if(page_history[i] === "vol_cases_details"){
                page_history.unshift("vol_cases_details");
                break;
            }else continue;
        }
    }
}

function close(){
    if(page_history[0] === "search_cases_details" || page_history[0] === "top_nav_search_user_profile" || page_history[0] === "vol_cases_details" || page_history[0] === "top-nav-action_centre_assign_case" || page_history[0] === "top-nav-action_centre_close_case"){
        document.getElementById('top-nav-search-results').style.display = "none";
        document.getElementById("user-profile").style.display = "none";
        document.getElementById("case_profile").style.display = "none";
        document.getElementById('top-nav-case-details').style.display = "none";
        document.getElementById('top-nav-case-details-form').style.display = "none";
        document.getElementById('top-nav-case-details-form').style.pointerEvents = "";
        document.getElementById('top-nav-case-details-form').style.opacity = "1";
        document.getElementById('top-nav-content-assign-case').style.display = "none";
        document.getElementById('top-nav-content-close-case').style.display = "none";
        for (let i = 0; i < page_history.length; i++) {
            if (page_history[i] === "raise_a_case") {
                raise_a_case();
                page_history.unshift("raise_a_case");
                break;
            } else if (page_history[i] === "my_cases") {
                my_cases();
                page_history.unshift("my_cases");
                break;
            } else if (page_history[i] === "myc_case_details"){
                $("#case-details").css("display","block");
                $("#case-details-form").css("display","flex");
                page_history.unshift("myc_case_details");
                break;
            } else if (page_history[i] === "user_data_change_approvals") {
                user_data_change_approvals();
                page_history.unshift("user_data_change_approvals");
                break;
            } else if (page_history[i] === "action_centre_assign_case") {
                document.getElementById('content-assign-case').style.display = "block";
                page_history.unshift("action_centre_assign_case");
                break;
            } else if (page_history[i] === "action_centre_close_case") {
                document.getElementById('content-close-case').style.display = "block";
                page_history.unshift("action_centre_close_case");
                break;
            }
        }
    layer_change("layer1");
    } else if(page_history[0] === "myc_case_details" || page_history[0] === "action_centre_assign_case" || page_history[0] === "action_centre_close_case"){
        my_cases();
    }
}

function show_vol_tooltip(event) {
    clearTimeout(hide_tooltip_timeOut);
    var userId = $(event.srcElement).parent().find("#vol_id").val();
    var userInfo = userIdVsInfoMap[userId];
    $('#tooltip-info-name').text(userInfo.userName);
    $('#tooltip-info-role').text(userInfo.role);
    $('#address-line1').text(userInfo.mobile);
    $('#address-line2').text(userInfo.email);
    $('#tooltip-info-tel-number').text(userInfo.mobile);
    if (userInfo.image) {
        $('#tooltip-dp').html('<img src="data:image/png;base64,"' + userInfo.image + '><div id="decoy-for-img"></div>');
    } else {
        $('#tooltip-dp').html('<img id="dp_image" src="https://upload.wikimedia.org/wikipedia/commons/7/7c/User_font_awesome.svg" width="auto" height="70vh"><div id="decoy-for-img"></div>');
    }
    $("#vol-description-tooltip").fadeIn();
    var tooltip = document.getElementById("vol-description-tooltip").getBoundingClientRect();
    var tooltipHeight = tooltip.height;
    document.getElementById("vol-description-tooltip").style.top = event.target.getBoundingClientRect().top - tooltipHeight / 2.2 + "px";
    document.getElementById("vol-description-tooltip").style.left = event.target.getBoundingClientRect().left + 10 + event.target.getBoundingClientRect().width + "px";
}

function hide_vol_tooltip() {
    hide_tooltip_timeOut = setTimeout(function () {
        $("#vol-description-tooltip").fadeOut();
        $("#vol-description-tooltip").click();
        // $("#vol-description-tooltip").css("display", "none");
        $('#tooltip-info-name').text("");
        $('#tooltip-info-role').text("");
        $('#address-line1').text("");
        $('#address-line2').text("");
        $('#tooltip-pincode').text("");
        $('#tooltip-info-tel-number').text("");
        $('#tooltip-dp').html('<img id="dp_image" src="" width="0" height="0"><div id="decoy-for-img"></div>');
    }, 200);
}

function getVolInfo(param) {
    var formData = {
        'caseId': currentCaseId
    };
    $.ajax({
        type: 'GET',
        url: '/getVolInfo',
        data: formData,
        dataType: 'json'
    })
        .done(function (data) {
            var cc = [];
            $.each(JSON.parse(data.top5), function (i, item) {
                var html = aVol + item.userName + bVol + cVol + item.userId + dVol;
                userIdVsInfoMap[item.userId] = item;
                cc.push(html);
            });
            $('#'+param+'top_five_vol').html(cc.join(""));

            cc = [];
            $.each(JSON.parse(data.nearest), function (i, item) {
                var html = aVol + item.userName + bVol + cVol + item.userId + dVol;
                userIdVsInfoMap[item.userId] = item;
                cc.push(html);
            });
            $('#'+param+'nearest_5_vol').html(cc.join(""));
        });
}

function assignCaseReq(caseId, userId, param) {
    var formData = {
        'caseId': caseId,
        'userId': userId
    };

    // process the form
    $.ajax({
        type: 'POST',
        url: '/assignCase',
        data: formData,
        //dataType: 'json'
    })

        .done(function (data) {
            if(param === ''){
                $('#assign-case-success').text("Success! Case " + caseId + " assigned successfully to " + userId);
                $('#assign-case-success').fadeIn(200);
                $("#content-assign-case").css("pointerEvents", "none");
                document.getElementById("card-right-side").style.pointerEvents = "none";
                setTimeout(function () {
                    navigate_back();
                    navigate_back();
                    document.getElementById("card-right-side").style.pointerEvents = "";
                }, 1200);
            } else {
                $('#top-nav-assign-case-success').text("Success! Case " + caseId + " assigned successfully to " + userId);
                $('#top-nav-assign-case-success').fadeIn(200);
                $("#top-nav-content-assign-case").css("pointerEvents", "none");
                setTimeout(function () {
                    navigate_back();
                    navigate_back();
                }, 1200);
            }
        });
}

function closeCaseReq(caseId) {
    var formData = {
        'caseId': caseId,
        'closeRemark': $("[name='closeRemark']").val(),
        'closeCaseReason': $("[name='closeCaseReason']").val()
    };
    // process the form
    $.ajax({
        type: 'POST',
        url: '/closeCase',
        data: formData,
        // dataType: 'json'
    }).done(function (data) {
            navigate_back();
            navigate_back();
        });
}

function action_centre_assign_case(param){
    document.getElementById(param+'case-details-form').style.display = "none";
    document.getElementById(param+'content-assign-case').style.display = "block";
    page_history.unshift(param+"action_centre_assign_case");
    var caseId = $("#"+param+"case-id-case-details").val();
    if(param === "top-nav-"){
        layer_change('layer2');
        $("#top-nav-heading-text").html("Case Details | Case No.: " + caseId + " | Assign Case");
    } else {
        layer_change('layer1');
        $("#heading-text").html("Case Details | Case No.: " + caseId + " | Close Case");
    }
    getVolInfo(param);
}

function action_center_close_case(param){
    document.getElementById(param+"close-case-reason").selectedIndex = 0;
    document.getElementById(param+'case-details-form').style.opacity = "0.2";
    document.getElementById(param+'case-details-form').style.pointerEvents = "none";
    document.getElementById(param+'content-close-case').style.opacity = "1";
    document.getElementById(param+'content-close-case').style.display = "block";
    page_history.unshift(param+"action_centre_close_case");
    var caseId = $("#"+param+"case-id-case-details").val();
    if(param === "top-nav-"){
        layer_change('layer2');
        $("#top-nav-heading-text").html("Case Details | Case No.: " + caseId + " | Assign Case");
    } else {
        layer_change('layer1');
        $("#heading-text").html("Case Details | Case No.: " + caseId + " | Assign Case");
    }
    other_reason_close_case(param);
}

function action_center_history(){
    var caseId = $("#case-id-case-details").val();
    //layer_change('layer1');
    //$("#heading-text").html("Case Details | Case No.: " + caseId + " | History");
    var formData = {
        'caseId': currentCaseId
    };
    var a = '<tr>';
    var b = '<td class="tg-0lax">';
    var c = '</td>';
    var d = '</tr>';
    var tableHeader = '<tr><th class="tg-baqh">Sr</th><th class="tg-0lax">From User</th><th class="tg-0lax">To User</th><th class="tg-0lax">Ack</th><th class="tg-0lax">Amount</th><th class="tg-0lax">Description</th><th class="tg-0lax">Transfer Date</th></tr>';
    $.ajax({
        type: 'GET',
        url: '/getCaseTxn',
        data: formData,
        dataType: 'json'
    })
        .done(function (data) {
            $('#history-dialog-table').html('');
            var cc = [];
            cc.push(tableHeader);
            $.each(data, function (i, item) {
                var html = a+b+(i+1)+c;
                html += b+item.fromUser+'('+item.fromUserRole+')'+c;
                html += b+item.toUser+'('+item.toUserRole+')'+c;
                html += b+(item.isAck == 1? "Yes": item.isAck == -1 ? "No":"Pending")+c;
                html += b+item.amount+c;
                html += b+item.desc+c;
                html += b+item.transferDate+c;
                html += d;
                cc.push(html);
            });
            $('#history-dialog-table').html(cc.join(""));
        });
    //$('#history-dialog').show();
    $("#history-dialog").dialog({
        width: 800,  height: 600
    });
}

function close_case_validation(param){
    if (document.getElementById(param+'close-case-reason').value === "Other" && document.getElementById('close-case-other-reason').value === "") {
        document.getElementById(param+'close-case-other-reason').style.border = "2px solid red";
        document.getElementById(param+'close-case-other-reason').placeholder = "You must specify a Reason";
    }
    else if (document.getElementById(param+'close-case-reason').value === "") document.getElementById(param+'close-case-reason').style.border = "2px solid red";
    else closeCaseReq(currentCaseId);
}

$(document).ready(function () {

//-----Vol-Tooltip-----------------------------------------------------------------------------------
    $('.top-five-and-nearest-and-search-results-ul').on("mouseover", ".vol_tooltip", show_vol_tooltip);
    $('.top-five-and-nearest-and-search-results-ul').on("mouseout", ".vol_tooltip", hide_vol_tooltip);
    // $('#myc-body').click('/activeCases:table1', responseHandler);
    $('#active-tab-header').click('/activeCases:table1', responseHandler);
    $('#recent-tab-header').click('/recentCases:table2', responseHandler);
    $('#closed-tab-header').click('/closedCases:table3', responseHandler);

//------Right-Side-Cards-Toggle-Display--------------------------------------------------------------------------------------
    document.getElementById('rac-body').style.borderBottom = "5px solid #007AEA";   //Raise a Case is the Default Screen
    document.getElementById('rac-body').classList.add('right-side-card-rotate');    //Raise a Case is the Default Screen
    document.getElementById('rac-body').onclick = function () {
        raise_a_case();
    };
    document.getElementById('myc-body').onclick = function () {
        my_cases();
    };
    document.getElementById('usdca-body').onclick = function () {
        user_data_change_approvals();
    };

//------Active-Recent-Closed-Tabs-JS--------------------------------------------------------------------------------
    document.getElementById('active-tab-header').onclick = function my_cases_search_results_active() {
        this.classList.add('tab-header-animation');
        document.getElementById('recent-tab-header').classList.remove('tab-header-animation');
        document.getElementById('closed-tab-header').classList.remove('tab-header-animation');
        document.getElementById('active-tab-content').classList.add('tab-content-animation');
        document.getElementById('recent-tab-content').classList.remove('tab-content-animation');
        document.getElementById('closed-tab-content').classList.remove('tab-content-animation');
        document.getElementById('active-tab-content').style.display = "block";
        document.getElementById('recent-tab-content').style.display = "none";
        document.getElementById('closed-tab-content').style.display = "none";
    };

    document.getElementById('recent-tab-header').onclick = function my_cases_search_results_recent() {
        this.classList.add('tab-header-animation');
        document.getElementById('active-tab-header').classList.remove('tab-header-animation');
        document.getElementById('closed-tab-header').classList.remove('tab-header-animation');
        document.getElementById('active-tab-header').classList.remove('tab-content-animation');
        document.getElementById('active-tab-content').classList.remove('tab-content-animation');
        document.getElementById('recent-tab-content').classList.add('tab-content-animation');
        document.getElementById('recent-tab-content').style.display = "block";
        document.getElementById('closed-tab-content').style.display = "none";
        document.getElementById('active-tab-content').style.display = "none";
    };

    document.getElementById('closed-tab-header').onclick = function my_cases_search_results_closed() {
        this.classList.add('tab-header-animation');
        document.getElementById('active-tab-header').classList.remove('tab-header-animation');
        document.getElementById('recent-tab-header').classList.remove('tab-header-animation');
        document.getElementById('active-tab-content').classList.remove('tab-content-animation');
        document.getElementById('recent-tab-content').classList.remove('tab-content-animation');
        document.getElementById('closed-tab-content').classList.add('tab-content-animation');
        document.getElementById('closed-tab-content').style.display = "block";
        document.getElementById('active-tab-content').style.display = "none";
        document.getElementById('recent-tab-content').style.display = "none";
    };

//-----Top-Nav-Search-----------------------------------------------------------------------------------
    /*document.getElementById("top-nav-search").onkeyup = function () {
          if (this.value !== "") {
            document.getElementById('raise-a-case-content').style.opacity = 0;
            document.getElementById('my-cases-content').style.opacity = 0;
            document.getElementById('case-details').style.opacity = 0;
            document.getElementById('user-data-change-approvals-content').style.opacity = 0;
            document.getElementById('heading-raise-a-case').style.opacity = 0;
            document.getElementById('heading-my-cases').style.opacity = 0;
            document.getElementById('heading-case-details').style.opacity = 0;
            document.getElementById('heading-user-data-change-approvals').style.opacity = 0;
            document.getElementById('top-nav-search-heading').style.display = "block";
            document.getElementById('top-nav-search-results').style.display = "block";
            document.getElementById('heading-top-nav-case-details').style.display = "none";
            document.getElementById("user-profile").style.display = "none";
            document.getElementById('top-nav-case-details').style.display = "none";
            document.getElementById('top-nav-case-details-form').style.display = "none";
            document.getElementById("top-nav-search-heading-content").innerHTML = "Searching results for - #Search_Input";
            document.getElementById("top-nav-search-user-profile-back-button").style.display = "none";
            page_history.unshift("top_nav_search_results");
        } else {
            document.getElementById('raise-a-case-content').style.opacity = 1;
            document.getElementById('my-cases-content').style.opacity = 1;
            document.getElementById('case-details').style.opacity = 1;
            document.getElementById('user-data-change-approvals-content').style.opacity = 1;
            document.getElementById('heading-raise-a-case').style.opacity = 1;
            document.getElementById('heading-my-cases').style.opacity = 1;
            document.getElementById('heading-case-details').style.opacity = 1;
            document.getElementById('heading-user-data-change-approvals').style.opacity = 1;
            document.getElementById("top-nav-search").value = "";
            document.getElementById('top-nav-search-heading').style.display = "none";
            document.getElementById('top-nav-search-results').style.display = "none";
            document.getElementById("user-profile").style.display = "none";
            document.getElementById("top-nav-search-heading-content").innerHTML = "";
            if(document.getElementById('raise-a-case-content').style.display === "block"){page_history.unshift("raise_a_case")}
            else if(document.getElementById('my-cases-content').style.display === "block"){page_history.unshift("my_cases")}
            else if(document.getElementById('user-data-change-approvals-content').style.display === "block"){page_history.unshift("user_data_change_approvals")}
        }
    };*/
//---------Close-Button---Back-Button---------------------------------------------------
    $('#close-button, #top-nav-close-button').click(function(){
        close();
    });
    $('#back-button, #top-nav-back-button').click(function(){
        navigate_back();
    });

//------Search-User-Profile-Active-Recent-Closed-Tabs-JS--------------------------------------
    document.getElementById('user-profile-active-tab-header').onclick = function user_profile_search_results_active() {
        this.classList.add('tab-header-animation');
        document.getElementById('user-profile-recent-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-closed-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-active-tab-content').classList.add('tab-content-animation');
        document.getElementById('user-profile-recent-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-closed-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-active-tab-content').style.display = "block";
        document.getElementById('user-profile-recent-tab-content').style.display = "none";
        document.getElementById('user-profile-closed-tab-content').style.display = "none";
    };

    document.getElementById('user-profile-recent-tab-header').onclick = function user_profile_search_results_recent() {
        this.classList.add('tab-header-animation');
        document.getElementById('user-profile-active-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-closed-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-recent-tab-content').classList.add('tab-content-animation');
        document.getElementById('user-profile-active-tab-header').classList.remove('tab-content-animation');
        document.getElementById('user-profile-active-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-recent-tab-content').style.display = "block";
        document.getElementById('user-profile-closed-tab-content').style.display = "none";
        document.getElementById('user-profile-active-tab-content').style.display = "none";
    };

    document.getElementById('user-profile-closed-tab-header').onclick = function user_profile_search_results_closed() {
        this.classList.add('tab-header-animation');
        document.getElementById('user-profile-active-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-recent-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-closed-tab-content').classList.add('tab-content-animation');
        document.getElementById('user-profile-active-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-recent-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-closed-tab-content').style.display = "block";
        document.getElementById('user-profile-active-tab-content').style.display = "none";
        document.getElementById('user-profile-recent-tab-content').style.display = "none";
    };
//------Basic-Details-and-User-Cases------------------------------------------------------------
    document.getElementById("user-profile-basic-details-btn").onclick = function() {user_profile_basic_details();};
    document.getElementById("user-profile-user-cases-btn").onclick = function() {user_profile_user_cases();};

//------DropDown-Menu-JS--------------------------------------------------------------------------------
    var dropdown_menu_state = true;
    document.getElementById("more-btn").onclick = function dropdown_menu() {
        dropdown_menu_state = !dropdown_menu_state;
        if (dropdown_menu_state === true) {
            document.getElementById("dropdown-menu").style.display = "none";
            $("main").css("opacity", "1");
            $("#card-right-side").css("opacity", "1");
        } else {
            document.getElementById("dropdown-menu").style.display = "block";
            $("main").css("opacity", "0.1");
            $("#card-right-side").css("opacity", "0.1");
            document.getElementsByTagName("main")[0].style.pointerEvents = "none";
            document.getElementById("card-right-side").style.pointerEvents = "none";
        }
    };

    window.onclick = function (e) {
        if (!e.target.matches('#more-btn')) {
            if (dropdown_menu_state === false) {
                document.getElementById("dropdown-menu").style.display = "none";
                $("main").css("opacity", "1");
                $("#card-right-side").css("opacity", "1");
                document.getElementsByTagName("main")[0].style.pointerEvents = "";
                document.getElementById("card-right-side").style.pointerEvents = "";
                dropdown_menu_state = !dropdown_menu_state;
            }
        }
    };

//------Bird-or-Animal-Affected-Changes--&--Reset-Affected-Changes----------------------------------------------
    document.getElementById('bird-or-animal1').onclick = function () {  //Bird
        document.getElementById("animal-type").selectedIndex = 0;
        document.getElementById('header-form-left-side').innerHTML = "Bird Information";
        document.getElementById('animal-type-js').innerHTML = "Bird Type";
        document.getElementById('animal-name-js').innerHTML = "Bird Name";
        document.getElementById('animal-name').setAttribute("placeholder", "Eg: Cannary (Optional)");
        document.getElementById("raise-a-case-select-default").text = "Choose Bird Type:";
        $('.select-bird-class').css("display", "block");
        $('.select-animal-class').css("display", "none");
        $('#add-new-bird-animal').css("display", "block");
        $('#add-bird-animal').css("display", "none");
    };

    document.getElementById('bird-or-animal2').onclick = function () {  //Animal
        document.getElementById("animal-type").selectedIndex = 0;
        document.getElementById('header-form-left-side').innerHTML = "Animal Information";
        document.getElementById('animal-type-js').innerHTML = "Animal Type";
        document.getElementById('animal-name-js').innerHTML = "Animal Name";
        document.getElementById('animal-name').setAttribute("placeholder", "Eg: Rufus (Optional)");
        document.getElementById("raise-a-case-select-default").text = "Choose Animal Type:";
        $('.select-bird-class').css("display", "none");
        $('.select-animal-class').css("display", "block");
        $('#add-new-bird-animal').css("display", "block");
        $('#add-bird-animal').css("display", "none");
    };

    document.getElementById("animal-type").onchange = function(){
        if(document.getElementById("animal-type").value === "add-new-bird-animal") document.getElementById("add-bird-animal").style.display = "block";
        else document.getElementById("add-bird-animal").style.display = "none";
    };

    document.getElementById('reset-raise-case').onclick = function () {
        document.getElementById('header-form-left-side').innerHTML = "Bird/Animal Information";
        document.getElementById('animal-type-js').innerHTML = "Bird/Animal type: ";
        document.getElementById('animal-name-js').innerHTML = "Bird/Animal name: ";
        document.getElementById('animal-name').setAttribute("placeholder", "");
        $('.select-bird-class').css("display", "none");
        $('.select-animal-class').css("display", "none");
        $('#add-new-bird-animal').css("display", "none");
        $('#add-bird-animal').css("display", "none");
    };

//------Search-Focus-&-Clear-Table-on-Search------------------------------------------------------------------
    document.getElementById('search-case-input').onkeyup = function () {
        if (document.getElementById('search-case-input').value !== "") {
            $('#tab-header').fadeOut(300);
            hideresults();
        } else {
            $('#tab-header').fadeIn(300);
            showresults();
        }
    };


//-----+91-022-Validation------------------------------------------------------------------------------------
    document.getElementById("nine-one").onchange = function(){
        if(document.getElementById("nine-one").value === "+91"){
            document.getElementById("contact-number").setAttribute("max", "99999999");
            document.getElementById("contact-number").setAttribute("min", "50000000");
            document.getElementById("contact-number").setAttribute("title", "Valid 10 digit mobile number without prefix");
        }
        else {
            document.getElementById("contact-number").setAttribute("max", "99999999");
            document.getElementById("contact-number").setAttribute("min", "10000000");
            document.getElementById("contact-number").setAttribute("title", "Valid 8 digit landline number without prefix");
        }
    };

//--Action-Centre--------------------------------------------------------------------------------
    document.getElementById('action-center-assign-case').onclick = function(){action_centre_assign_case("");};
    document.getElementById('top-nav-action-center-assign-case').onclick = function(){action_centre_assign_case("top-nav-");};

    document.getElementById('action-center-close-case').onclick = function () {action_center_close_case("");};
    document.getElementById('top-nav-action-center-close-case').onclick = function () {action_center_close_case("top-nav-");};

    document.getElementById('action-center-history-case').onclick = function () {action_center_history();};
    document.getElementById('top-nav-action-center-history-case').onclick = function () {action_center_history();};

//Assign-Case------------------------------------------------------------------------------------
    document.getElementById('assign-case-search-input').onkeyup = function () {
        if (this.value !== "") {
            $('#top-five').fadeOut(200);
            $('#nearest-assigned-volunteers').fadeOut(200);
            setTimeout(function () {
                $('#assign-case-search-results').css('display', 'block');
                $('#assign-case-search-results').fadeIn(200);
            }, 200);
        } else {
            $('#top-five').fadeIn(200);
            $('#nearest-assigned-volunteers').fadeIn(200);
            $('#assign-case-search-results').css('display', 'none');
            $('#assign-case-search-results').fadeOut(200);
        }
    };
    document.getElementById('top-nav-assign-case-search-input').onkeyup = function () {
        if (this.value !== "") {
            $('#top-nav-top-five').fadeOut(200);
            $('#top-nav-nearest-assigned-volunteers').fadeOut(200);
            setTimeout(function () {
                $('#top-nav-assign-case-search-results').css('display', 'block');
                $('#top-nav-assign-case-search-results').fadeIn(200);
            }, 200);
        } else {
            $('#top-nav-top-five').fadeIn(200);
            $('#top-nav-nearest-assigned-volunteers').fadeIn(200);
            $('#top-nav-assign-case-search-results').css('display', 'none');
            $('#top-nav-assign-case-search-results').fadeOut(200);
        }
    };

    document.getElementById('close-case-reason').onchange = function () {other_reason_close_case("");};
    document.getElementById('top-nav-close-case-reason').onchange = function () {other_reason_close_case("top-nav-");};

//------Close-Case-Button--aka--Submit-Close-Case----------------------------------------------------------------------
    document.getElementById('close-case-submit-btn').onclick = function () {close_case_validation("");};
    document.getElementById('top-nav-close-case-submit-btn').onclick = function () {close_case_validation("top-nav-");};

    document.getElementById('close-case-other-reason').addEventListener("keyup", function () {  //Removes Red border while typing
        document.getElementById('close-case-other-reason').style.border = "1px solid #666";
    });
    document.getElementById('top-nav-close-case-other-reason').addEventListener("keyup", function () {  //Removes Red border        while typing
        document.getElementById('top-nav-close-case-other-reason').style.border = "1px solid #666";
    });

//------Keyboard-Shortcuts--------------------------------------------------------------------------------------------------
    document.onkeyup = function (e) {
        if (e.ctrlKey && e.altKey && e.which == 83) {
            if (document.getElementById('heading-text').innerHTML === "Raise a Case") {
                document.getElementById('submit-raise-case').click();
            } else alert("Ctrl + Alt + S is shortcut for Raising a Case");
        }
        if (e.ctrlKey && e.altKey && e.which == 82) {
            if (document.getElementById('heading-text').innerHTML === "Raise a Case") {
                document.getElementById('reset-raise-case').click();
            } else alert("Ctrl + Alt + R is shortcut for Resetting all fields of a Case");
        }
    };

//-----Disable-Alt+Left+Arrow----and------Disbale-Alt+Right+Arrow--------------------------------------------------------------
    document.onkeydown = function (e) {
        if (e.altKey && e.which == 37 || e.altKey && e.which == 39) e.preventDefault();
    };

//------Approve-Reject-Buttons-Effect-------------------------------------------------------------------------------------
    for (let i = 0; i < document.getElementsByClassName("approve").length; i++) {
        document.getElementsByClassName("approve")[i].onclick = function () {
            if (event.target.tagName == "IMG" && event.target.parentElement.parentElement.classList.contains('approve')) {
                event.target.setAttribute('src', '/img/approved-click.png');
                $(event.target.parentElement.parentElement.parentElement.parentElement).fadeOut();
            } else return false;
        }
    }

    for (let i = 0; i < document.getElementsByClassName("reject").length; i++) {
        document.getElementsByClassName("reject")[i].onclick = function() {
            if (event.target.tagName == "IMG" && event.target.parentElement.parentElement.classList.contains('reject')) {
                event.target.setAttribute('th:src', '@{/img/rejected-click.png}');
                $(event.target.parentElement.parentElement.parentElement.parentElement).fadeOut();
            } else alert("Error");
        };
    }

//------Enlarge-Old-or-New-Photo---------------------------------------------------------------------------------------
    $('.old-photo').children("figure").children("img").click(function () {
        enlargePhoto(this);
    });
//----User-Profile-DP-Enlarge-------------------------------------------------
    $('#user-profile-left-side, .new-photo').children('img').click(function () {
        enlargePhoto(this);
    });

    function enlargePhoto(this_t) {
        $('#dynamic_image_enlarge').css('display', 'block');
        var enlarge_source = this_t.getAttribute('src');
        $('#photo-enlarge').children('img').attr('src', enlarge_source);
        $('#photo-enlarge').children('img').css("background-color", "#f4f4f4");
    }
    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', 'none');
    });

//-----Disable-Right-Click-Menu-On-Image----------------------------------------------------------------------
    $("body").on("contextmenu", "img", function (e) {
        return false;
    });

//-----Refresh-Button-Top-Nav---------------------------------------------------------------------------------
    document.getElementById("home-btn").onclick = function (e) {
        e.preventDefault();
        window.location.reload(true);
    };

//------Tooltip-Vol-Assign-Case-------------------------------------------------------------------------------
    document.getElementById("vol-description-tooltip").onmouseover = function (e) {
        clearTimeout(hide_tooltip_timeOut);
        $("#vol-description-tooltip").css("display", "block");
    };
    document.getElementById("vol-description-tooltip").onmouseout = function (e) {hide_vol_tooltip();};

//------BACKEND-INTEGRATION-----------------------------------------------------------------------------------
    $('#raise-a-case-form').submit(function (event) {
        event.preventDefault();
        console.log(document.getElementById("contact-number").checkValidity());
        let formData = {
            'typeAnimal': $('select[name=animal-type]').val(),
            'animalName': $('input[name=animal-name]').val(),
            'animalCondition': $('input[name=condition]').val(),
            'contactName': $('input[name=contact-name]').val(),
            'contactNumber': $('input[name=contact-number]').val(),
            'location': $('textarea[name=location]').val(),
            'locationPincode': $('input[name=location-pincode]').val(),
            'locationLandMark': $('input[name=location-landmark]').val(),
            'contactPrefix': $('select[name=nine-one]').val(),
            'birdOrAnimal': $('input[name=bird-or-animal]').val(),
            'newBirdAnimal': $('input[name=add-bird-animal]').val()
        };

        $.ajax({
            type: 'POST', // define the type of HTTP verb we want to use (POST for our form)
            url: '/addNewCase', // the url where we want to POST
            data: formData, // our data object
            //dataType: 'json' // what type of data do we expect back from the server
            //encode: true
        })
        // using the done promise callback
            .done(function (data) {
                if (data && data == 'error') {
                    $('#error').text(data);
                    return;
                } else if($('#case-photos').val()) {
                    var form = $('#raise-a-case-form')[0];
                    var formData = new FormData(form);
                    formData.append("case_id", data);
                    $.ajax({
                        url: 'casePicUpload',
                        type: 'POST',
                        data: formData,
                        enctype: 'multipart/form-data',
                        processData: false,  // tell jQuery not to process the data
                        contentType: false,  // tell jQuery not to set contentType
                        success: function (data) {
                            console.log(data);
                        },
                        error: function (e) {
                            console.log(e);
                        }
                    });
                }

                if(data) {
                    $('#raise-a-case-form')[0].reset();
                    document.getElementById("reset-raise-case").click();
                    $('#case-id').val(data);
                    $('#raise-a-case-success').fadeIn();
                    $('#raise-a-case-content').css("pointerEvents","none");
                    currentCaseId = data;
                    setTimeout(function () {
                        $('#case-id').val('');
                        $('#raise-a-case-success').fadeOut();
                        $('#raise-a-case-content').css("pointerEvents","all");
                    }, 1500);
                }
                else {
                    $('#raise-a-case-error').fadeIn();
                }
            });

    });

    var container = $(document.createElement('div')).css({});
    $("#search-case-input").autocomplete({
        minLength: 1,
        delay: 500,
        source: function (request, response) {
            $.getJSON("/getCaseInfoForSearch", request, function (result) {
                response($.map(result, function (item) {
                    return {
                        label: item.caseId,
                        value: item.caseId,
                        caseDetails: item
                    }

                }));
            });
        },

        select: function (event, ui) {
            if (ui.item) {
                caseDetails = ui.item.caseDetails;
                caseIdVsInfoMap[caseDetails.caseId] = caseDetails;
                var htm = aCase + caseDetails.caseId + bCase + (caseDetails.userNameCurrent ? caseDetails.userNameCurrent+"("+caseDetails.userRoleCurrent+")" : "Closed" )  + b1Case+(ui.item.isAck == 1 ? "Yes" : ui.item.isAck == -1 ? "No" : "Pending" ) + cCase + caseDetails.creationDateStr + dCase + caseDetails.typeAnimal + eCase + caseDetails.animalName + fCase;
                $(container).prepend(htm);
                $('#table4').html(container);
                $("#search-case-input").autocomplete("close");
                $("#search-case-input").val('');
                $('.my-cases-result1').show();
            }
        }

    });

    $("#assign-case-search-input, #top-nav-assign-case-search-input").autocomplete({
        minLength: 2,
        delay: 500,
        source: function (request, response) {
            $("#assign_case_loading").css("display", "block");
            $.getJSON("/getVolListForSearch", request, function (result) {
                if(page_history === "top-nav-action_centre_assign_case") $("#top-nav-assign_case_loading").css("display", "none");
                else $("#assign_case_loading").css("display", "none");
                response($.map(result, function (item) {
                    return {
                        // following property gets displayed in drop down
                        label: item.userName,
                        // following property gets entered in the textbox
                        value: item.userName,
                        // following property is added for our own use
                        userDetails: item
                    }

                }));
            });
        },

        select: function (event, ui) {
            if (ui.item) {
                var html;
                userIdVsInfoMap[ui.item.userDetails.userId] = ui.item.userDetails;
                if(page_history[0] === "top-nav-action_centre_assign_case"){
                    html = top_nav_a_Vol + ui.item.userDetails.userName + top_nav_b_Vol + top_nav_c_Vol + ui.item.userDetails.userId + top_nav_d_Vol;
                    $('#top-nav-search_vol').html(html);
                    $("#top-nav-assign-case-search-input").autocomplete("close");
                    $("#top-nav-assign-case-search-input").val('');
                }
                else {
                    html = aVol + ui.item.userDetails.userName + bVol + cVol + ui.item.userDetails.userId + dVol;
                    $('#search_vol').html(html);
                    $("#assign-case-search-input").autocomplete("close");
                    $("#assign-case-search-input").val('');
                }
                return false;
            }
        }

    });

    $("#top-nav-search").autocomplete({
       minLength: 1,
        delay: 500,
        source:
            function (request, response) {
                $.getJSON("/getCaseVolListForSearch", request, function (result) {
                    response($.map(result, function (item) {
                        if (item.caseId) {
                            return {
                                label: "Case : " + item.caseId+" - "+item.typeAnimal+" - "+item.animalName,
                                value: item.caseId,
                                caseDetails: item
                            }
                        } else {
                            return {
                                label: item.role +" - "+ item.userName,
                                value: item.userName,
                                userDetails: item
                            }
                        }

                    }));
                });
            },
        select: function (event, ui) {
            if (ui.item) {
                document.getElementById("user-profile").style.display = "none";
                if (ui.item.userDetails) {
                    var ud = ui.item.userDetails;
                    var html = aVol + ud.userName + bVol + cVol + ud.userId + dVol;
                    userIdVsInfoMap[ud.userId] = ud;
                    $('#user-profile-left-side').find('img').attr('src', "data:image/png;base64," + ud.userImage.image);
                    $('#user-profile-middle-side_user_name').text(ud.userName);
                    $('#user-profile-middle-side_user_role').text(ud.role);
                    $('#user-profile-right-side-table_ac').text(ud.caseAcceptedCount);
                    $('#user-profile-right-side-table_rc').text(ud.caseRejectedCount);

                    $('#user-profile-name').text(ud.firstName + " " + ud.lastName);
                    $('#user-profile-dob').text(ud.dob);
                    $('#user-profile-gender').text(ud.gender);
                    $('#user-profile-email').text(ud.email);

                    $('#huser-profile-address-line1').text(ud.homeAddr.addrLine1);
                    $('#huser-profile-address-line2').text(ud.homeAddr.addrLine2);
                    $('#huser-profile-address-pincode').text(ud.homeAddr.pincode);
                    $('#huser-profile-contact-number_prefix').text(ud.homeAddr.contactPrefix);
                    $('#huser-profile-contact-number').text(ud.homeAddr.contact);

                    $('#ouser-profile-address-line1').text(ud.officeAddr.addrLine1);
                    $('#ouser-profile-address-line2').text(ud.officeAddr.addrLine2);
                    $('#ouser-profile-address-pincode').text(ud.officeAddr.pincode);
                    $('#ouser-profile-contact-number_prefix').text(ud.officeAddr.contactPrefix);
                    $('#ouser-profile-contact-number').text(ud.officeAddr.contact);

                    $('#table11').html("");
                    $('#table22').html("");
                    $('#table33').html("");

                    $('#user-profile-user-cases-btn').text(ud.userName+"'s cases");

                    $('#user-profile-user-cases-btn').unbind('click');
                    $('#user-profile-active-tab-header').unbind('click');
                    $('#user-profile-recent-tab-header').unbind('click');
                    $('#user-profile-closed-tab-header').unbind('click');

                    $('#user-profile-user-cases-btn').click('/activeCases:table11:'+ud.userId, responseHandler);
                    $('#user-profile-active-tab-header').click('/activeCases:table11:'+ud.userId, responseHandler);
                    $('#user-profile-recent-tab-header').click('/recentCases:table22:'+ud.userId, responseHandler);
                    $('#user-profile-closed-tab-header').click('/closedCases:table33:'+ud.userId, responseHandler);

                    search_list_user.unshift(ud.userName);
                    top_nav_search_user_profile();
                    layer_change('layer2');
                    $("#top-nav-heading-text").html("Search User | " + ud.userName);
                    $("#top-nav-back-close-btn-wrapper").css("display","inline");
                } else if (ui.item.caseDetails) {
                    cd = ui.item.caseDetails;
                    caseIdVsInfoMap[cd.caseId] = cd;
                    $('#raise-a-case-content').hide();
                    $('#top-nav-case-id-case-details').val(cd.caseId);
                    $('#top-nav-animal-type-case-details').val(cd.typeAnimal);
                    $('#top-nav-animal-name-case-details').val(cd.animalName);
                    $('#top-nav-condition-case-details').val(cd.animalCondition);
                    $('#top-nav-case-photos-case-details').append('<img src="/img/loading-simple.gif" class="top_nav_case_details_loading_screen" alt="Loading">');
                    caseImageRetriever(cd.caseId,"top-nav-case-photos-case-details");
                    $('#top-nav-contact-name-case-details').val(cd.contactName);
                    $('#top-nav-nine-one-case-details').val(cd.contactPrefix);
                    $('#top-nav-contact-number-case-details').val(cd.contactNumber);
                    $('#top-nav-location-landmark-case-details').val(cd.locationLandMark);
                    $('#top-nav-location-pincode-case-details').val(cd.locationPincode);
                    layer_change('layer2');
                    $("#top-nav-heading-text").html("Case Details | Case No.: " + cd.caseId);
                    $("#top-nav-back-close-btn-wrapper").css("display","inline");
                    if(cd.birdOrAnimal === "Animal"){
                        $("#header-top-nav-form-left-side-case-details").html("Animal Information");
                        $("#top-nav-animal-bird-type-case-details").html("Animal Type: ");
                        $("#top-nav-animal-bird-name-case-details").html("Animal Name: ");
                    } else {
                        $("#header-top-nav-form-left-side-case-details").html("Bird Information");
                        $("#top-nav-animal-bird-type-case-details").html("Bird Type: ");
                        $("#top-nav-animal-bird-name-case-details").html("Bird Name: ");       
                    }
                    top_nav_search_case_details("search_cases_details");
                    layer_change('layer2');
                    $('#top-nav-heading-text').html('Case Details | Case No.: ' + cd.caseId);
                    /*if (cd.active) {
                        //$('#action-center').show();
                        $('#action-center-assign-case').show();
                        $('#action-center-close-case').show();
                    } else {
                        //$('#action-center').hide();
                        $('#action-center-assign-case').hide();
                        $('#action-center-close-case').hide();
                    }*/
                }
                $("#top-nav-search").autocomplete("close");
                $("#top-nav-search").val('');
                return false;
            }
        }

    });
});