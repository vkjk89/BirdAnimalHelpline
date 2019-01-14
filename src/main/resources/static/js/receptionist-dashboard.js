setTimeout(function () {
    let viewheight = $(window).height();
    let viewwidth = $(window).width();
    let viewport = document.querySelector("meta[name=viewport]");
    viewport.setAttribute("content", "height=" + viewheight + "px, width=" + viewwidth + "px, initial-scale=1.0");
}, 300);

var page_history = ["raise_a_case"];
function case_details(data) {
    document.getElementById('my-cases-content').style.display = "none";
    document.getElementById('case-details').style.display = "block";
    document.getElementById('heading-my-cases').style.display = "none";
    document.getElementById('heading-case-details').style.display = "block";
    document.getElementById('case-details-form').style.display = "flex";
    document.getElementById('action-center').style.display = "block";
    document.getElementById('case-details-form').style.opacity = "";
    page_history.unshift("myc_case_details");
    console.info(" vkj map : " + caseIdVsInfoMap);
    currentCaseId = $(data).find(".case-id").text();
    console.info("vkj id : " + currentCaseId);
    var caseInfo = caseIdVsInfoMap[currentCaseId];
    console.info(caseInfo);
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
    if(!caseInfo.active) {
        $('#action-center').hide();
    }
}

function raise_a_case() {
    document.getElementById('rac-body').classList.add('right-side-card-rotate');
    document.getElementById('rac-body').style.borderBottom = "5px solid #007AEA";
    document.getElementById('myc-body').classList.remove('right-side-card-rotate');
    document.getElementById('myc-body').style.borderBottom = "none";
    document.getElementById('usdca-body').classList.remove('right-side-card-rotate');
    document.getElementById('usdca-body').style.borderBottom = "none";
    setTimeout(function () {
        document.getElementById('rac-body').classList.remove('right-side-card-rotate');
    }, 600);

    page_history.unshift("raise_a_case");
    document.getElementById('raise-a-case-content').style.display = "block";
    document.getElementById('my-cases-content').style.display = "none";
    document.getElementById('user-data-change-approvals-content').style.display = "none";
    document.getElementById('heading-raise-a-case').style.display = "block";
    document.getElementById('heading-my-cases').style.display = "none";
    document.getElementById('heading-user-data-change-approvals').style.display = "none";
    document.getElementById('top-nav-search-heading').style.display = "none";
    document.getElementById('top-nav-search-results').style.display = "none";
    document.getElementById('raise-a-case-content').style.opacity = 1;
    document.getElementById('my-cases-content').style.opacity = 1;
    document.getElementById('case-details').style.opacity = 1;
    document.getElementById('user-data-change-approvals-content').style.opacity = 1;
    document.getElementById('heading-raise-a-case').style.opacity = 1;
    document.getElementById('heading-my-cases').style.opacity = 1;
    document.getElementById('heading-case-details').style.opacity = 1;
    document.getElementById('heading-user-data-change-approvals').style.opacity = 1;
    document.getElementById("top-nav-search").value = "";
    document.getElementById("user-profile").style.display = "none";
    document.getElementById("case_profile").style.display = "none";
    document.getElementById("top-nav-search-heading-content").innerHTML = "";
    document.getElementById('heading-case-details').style.display = "none";
    document.getElementById('case-details').style.display = "none";
    document.getElementById('top-nav-case-details').style.display = "none";
    document.getElementById('heading-top-nav-case-details').style.display = "none";
    document.getElementById('top-nav-case-details-form').style.display = "none";
}

function my_cases() {
    document.getElementById('myc-body').classList.add('right-side-card-rotate');
    document.getElementById('myc-body').style.borderBottom = "5px solid #007AEA";
    document.getElementById('rac-body').classList.remove('right-side-card-rotate');
    document.getElementById('rac-body').style.borderBottom = "none";
    document.getElementById('usdca-body').classList.remove('right-side-card-rotate');
    document.getElementById('usdca-body').style.borderBottom = "none";
    setTimeout(function () {
        document.getElementById('myc-body').classList.remove('right-side-card-rotate');
    }, 600);

    page_history.unshift("my_cases");
    document.getElementById('active-tab-header').classList.add('active-header');
    document.getElementById('raise-a-case-content').style.display = "none";
    document.getElementById('my-cases-content').style.display = "block";
    document.getElementById('search-case-input').value = "";
    document.getElementById('user-data-change-approvals-content').style.display = "none";
    document.getElementById('heading-raise-a-case').style.display = "none";
    document.getElementById('heading-my-cases').style.display = "block";
    document.getElementById('heading-user-data-change-approvals').style.display = "none";
    $('#tab-header').fadeIn(300);
    showresults();
    document.getElementById('raise-a-case-content').style.opacity = 1;
    document.getElementById('my-cases-content').style.opacity = 1;
    document.getElementById('case-details').style.opacity = 1;
    document.getElementById('user-data-change-approvals-content').style.opacity = 1;
    document.getElementById('heading-raise-a-case').style.opacity = 1;
    document.getElementById('heading-my-cases').style.opacity = 1;
    document.getElementById('heading-case-details').style.opacity = 1;
    document.getElementById('heading-user-data-change-approvals').style.opacity = 1;
    document.getElementById('top-nav-search-heading').style.display = "none";
    document.getElementById('top-nav-search-results').style.display = "none";
    document.getElementById("top-nav-search").value = "";
    document.getElementById("user-profile").style.display = "none";
    document.getElementById("case_profile").style.display = "none";
    document.getElementById("top-nav-search-heading-content").innerHTML = "";
    document.getElementById('heading-case-details').style.display = "none";
    document.getElementById('case-details').style.display = "none";
    document.getElementById('content-assign-case').style.display = "none";
    document.getElementById('content-close-case').style.display = "none";
    document.getElementById('case-details-form').style.pointerEvents = "";
    document.getElementById('top-nav-case-details').style.display = "none";
    document.getElementById('heading-top-nav-case-details').style.display = "none";
    document.getElementById('top-nav-case-details-form').style.display = "none";
    document.getElementById('active-tab-header').click();    
}

function user_data_change_approvals() {
    document.getElementById('usdca-body').classList.add('right-side-card-rotate');
    document.getElementById('usdca-body').style.borderBottom = "5px solid #007AEA";
    document.getElementById('rac-body').classList.remove('right-side-card-rotate');
    document.getElementById('rac-body').style.borderBottom = "none";
    document.getElementById('myc-body').classList.remove('right-side-card-rotate');
    document.getElementById('myc-body').style.borderBottom = "none";
    setTimeout(function () {
        document.getElementById('usdca-body').classList.remove('right-side-card-rotate');
    }, 600);

    page_history.unshift("user_data_change_approvals");
    document.getElementById('raise-a-case-content').style.display = "none";
    document.getElementById('my-cases-content').style.display = "none";
    document.getElementById('user-data-change-approvals-content').style.display = "block";
    document.getElementById('heading-raise-a-case').style.display = "none";
    document.getElementById('heading-my-cases').style.display = "none";
    document.getElementById('heading-user-data-change-approvals').style.display = "block";
    document.getElementById('top-nav-search-heading').style.display = "none";
    document.getElementById('top-nav-search-results').style.display = "none";
    document.getElementById('raise-a-case-content').style.opacity = 1;
    document.getElementById('my-cases-content').style.opacity = 1;
    document.getElementById('case-details').style.opacity = 1;
    document.getElementById('user-data-change-approvals-content').style.opacity = 1;
    document.getElementById('heading-raise-a-case').style.opacity = 1;
    document.getElementById('heading-my-cases').style.opacity = 1;
    document.getElementById('heading-case-details').style.opacity = 1;
    document.getElementById('heading-user-data-change-approvals').style.opacity = 1;
    document.getElementById("top-nav-search").value = "";
    document.getElementById("user-profile").style.display = "none";
    document.getElementById("case_profile").style.display = "none";
    document.getElementById("top-nav-search-heading-content").innerHTML = "";
    document.getElementById('heading-case-details').style.display = "none";
    document.getElementById('case-details').style.display = "none";
    document.getElementById('top-nav-case-details').style.display = "none";
    document.getElementById('heading-top-nav-case-details').style.display = "none";
    document.getElementById('top-nav-case-details-form').style.display = "none";
}

function search_close_button() {
    document.getElementById('raise-a-case-content').style.opacity = 1;
    document.getElementById('my-cases-content').style.opacity = 1;
    document.getElementById('case-details').style.opacity = 1;
    document.getElementById('user-data-change-approvals-content').style.opacity = 1;
    document.getElementById('heading-raise-a-case').style.opacity = 1;
    document.getElementById('heading-my-cases').style.opacity = 1;
    document.getElementById('heading-case-details').style.opacity = 1;
    document.getElementById('heading-user-data-change-approvals').style.opacity = 1;
    document.getElementById('content-assign-case').style.opacity = 1;
    document.getElementById('content-close-case').style.opacity = 1;
    document.getElementById("top-nav-search").value = "";
    document.getElementById('top-nav-search-heading').style.display = "none";
    document.getElementById('top-nav-search-results').style.display = "none";
    document.getElementById("user-profile").style.display = "none";
    document.getElementById("case_profile").style.display = "none";
    document.getElementById("top-nav-search-heading-content").innerHTML = "";
    document.getElementById('top-nav-case-details').style.display = "none";
    document.getElementById('heading-top-nav-case-details').style.display = "none";
    document.getElementById('top-nav-case-details-form').style.display = "none";
    for (let i = 0; i < page_history.length; i++) {
        if (page_history[i] === "raise_a_case") {
            raise_a_case();
            page_history.unshift("raise_a_case");
            break;
        } else if (page_history[i] === "my_cases") {
            my_cases();
            page_history.unshift("my_cases");
            break;
        } else if (page_history[i] === "user_data_change_approvals") {
            user_data_change_approvals();
            page_history.unshift("user_data_change_approvals");
            break;
        } else if (page_history[i] === "action_centre_assign_case") {
            document.getElementById('content-assign-case').style.display = "block";
            page_history.unshift("action_centre_assign_case");
            break;
        } else if (page_history[i] === "action_close_assign_case") {
            document.getElementById('content-close-case').style.display = "block";
            page_history.unshift("action_close_assign_case");
            break;
        }
    }
}

function top_nav_search_user_profile() {
    document.getElementById("top-nav-search").value = "";
    document.getElementById("top-nav-search-heading-content").innerHTML = "#Search_Input - #User_Name";
    document.getElementById("top-nav-search-results").style.display = "none";
    document.getElementById("user-profile").style.display = "block";
    document.getElementById("top-nav-search-user-profile-back-button").style.display = "block";
    document.getElementById("user-profile-cases-content").style.display = "none";
    page_history.unshift("top_nav_search_user_profile");
    $("#user-profile-about-content").fadeIn();
    user_profile_basic_details();
}

function top_nav_search_case_details(identifier1) {
    document.getElementById('top-nav-case-details').style.display = "block";
    document.getElementById('heading-top-nav-case-details').style.display = "block";
    document.getElementById('top-nav-case-details-form').style.display = "flex";
    document.getElementById("top-nav-search").value = "";
    document.getElementById('top-nav-search-heading').style.display = "none";
    document.getElementById('top-nav-search-results').style.display = "none";
    document.getElementById("user-profile").style.display = "none";
    document.getElementById("case_profile").style.display = "none";
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

function hideresults() {
    $('.row').fadeOut(300);
}

function showresults() {
    $('.row').fadeIn(300);
}


function assignCase(data) {
    console.info(data);
    var userId = $(data).parent().find("#vol_id").val();
    console.info(userId);
    assignCaseReq(currentCaseId, userId);
}

function other_reason_close_case() {
    if (document.getElementById('close-case-reason').value === "Other") {
        document.getElementById('close-case-other-reason').style.display = "block";
        document.getElementById('close-case-other-reason').value = "";
        document.getElementById('close-case-other-reason').innerHTML = "";
        document.getElementById('close-case-other-reason').style.border = "1px solid #666";
        document.getElementById('close-case-reason').style.border = "1px solid #666";
        document.getElementById('close-case-other-reason').placeholder = "Specify Reason";
    } else {
        document.getElementById('close-case-other-reason').style.display = "none";
        document.getElementById('close-case-reason').style.border = "1px solid #666";
    }
}

function navigate_back() {
    if (page_history[0] === "myc_case_details") {
        document.getElementById('my-cases-content').style.display = "block";
        document.getElementById('heading-my-cases').style.display = "block";
        document.getElementById('case-details').style.display = "none";
        document.getElementById('heading-case-details').style.display = "none";
        document.getElementById('case-details-form').style.display = "none";
        page_history.unshift("my_cases");
    } else if (page_history[0] === "action_centre_assign_case" || page_history[0] === "action_centre_close_case") {
        document.getElementById('case-details-form').style.display = "flex";
        document.getElementById('content-assign-case').style.display = "none";
        document.getElementById('content-close-case').style.display = "none";
        document.getElementById('myc-case-details-heading-animation').innerHTML = "Case Details";
        document.getElementById('assign-case-success').style.display = "none";
        $("#content-assign-case").css("pointerEvents", "");
        document.getElementById('case-details-form').style.opacity = "1";
        document.getElementById('top-nav-search-heading').style.display = "none";
        document.getElementById('top-nav-search-results').style.display = "none";
        document.getElementById('case-details-form').style.pointerEvents = "";
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
        document.getElementById('top-nav-search-heading').style.display = "block";
        document.getElementById('top-nav-search-results').style.display = "block";
        document.getElementById("top-nav-search-heading-content").innerHTML = "Searching results for - #Search_Input";
        document.getElementById("user-profile").style.display = "none";
        document.getElementById("case_profile").style.display = "none";
        document.getElementById("top-nav-search-user-profile-back-button").style.display = "none";
        page_history.unshift("top_nav_search_results");
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
        document.getElementById('heading-top-nav-case-details').style.display = "none";
        document.getElementById('top-nav-case-details-form').style.display = "none";
        document.getElementById('top-nav-search-heading').style.display = "block";
        document.getElementById("user-profile").style.display = "block";
        page_history.unshift("top_nav_search_user_profile");
    } else if (page_history[0] === "search_cases_details") {
        search_close_button();
    }
}

function show_vol_tooltip() {
    clearTimeout(hide_tooltip_timeOut);
    console.info("vimal   1");
    console.info(event.srcElement);
    var userId = $(event.srcElement).parent().find("#vol_id").val();
    console.info(" vkj : " + userId);
    var userInfo = userIdVsInfoMap[userId];
    $('#tooltip-info-name').text(userInfo.userName);
    $('#tooltip-info-role').text(userInfo.role);
    $('#address-line1').text(userInfo.mobile);
    $('#address-line2').text(userInfo.email);
    $('#tooltip-info-tel-number').text(userInfo.mobile);
    if (userInfo.image) {
        $('#tooltip-dp').html('<img src="data:image/png;base64,"' + userInfo.image + '><div id="decoy-for-img">.</div>');
    } else {
        $('#tooltip-dp').html('<img id="dp_image" src="https://upload.wikimedia.org/wikipedia/commons/7/7c/User_font_awesome.svg" width="auto" height="70vh"><div id="decoy-for-img">.</div>');
    }
    $("#vol-description-tooltip").fadeIn();
    var tooltipHeight = document.getElementById("vol-description-tooltip").getBoundingClientRect().height;
    document.getElementById("vol-description-tooltip").style.top = event.target.getBoundingClientRect().top - tooltipHeight / 2.2 + "px";
    document.getElementById("vol-description-tooltip").style.left = event.target.getBoundingClientRect().left + 10 + event.target.getBoundingClientRect().width + "px";
}

var hide_tooltip_timeOut;

function hide_vol_tooltip() {
    hide_tooltip_timeOut = setTimeout(function () {
        $("#vol-description-tooltip").fadeOut();
        $("#vol-description-tooltip").click();
        $("#vol-description-tooltip").css("display", "none");
        $('#tooltip-info-name').text("");
        $('#tooltip-info-role').text("");
        $('#address-line1').text("");
        $('#address-line2').text("");
        $('#tooltip-info-tel-number').text("");
        $('#tooltip-dp').html('<img id="dp_image" src="https://upload.wikimedia.org/wikipedia/commons/7/7c/User_font_awesome.svg" width="auto" height="70vh"><div id="decoy-for-img">.</div>');
    }, 200);
}

var aVol = "<li> <span class=\"vol_tooltip\">";
var bVol = "</span> <button onclick='assignCase(this)' type=\"button\" class=\"assign-case-assign-btn\">Assign</button>";
var cVol = "<input type=\"hidden\" id=\"vol_id\" value=\"";
var dVol = "\" ></li>";

var aCase = '<div class ="row row1" onclick="case_details(this)"> <div class="sr-no"><li></li></div> <div class="case-id">';
var bCase = '</div> <div class="case-status">';
var cCase = '</div> <div class="date">';
var dCase = '</div> <div class="animal-type">';
var eCase = '</div> <div class="case-name">';
var fCase = '</div> </div>';


var caseIdVsInfoMap = {};
var userIdVsInfoMap = {};
var currentCaseId = -1;

function getVolInfo() {

    var formData = {
        'caseId': currentCaseId
    };

    // process the form
    $.ajax({
        type: 'GET',
        url: '/getVolInfo',
        data: formData,
        dataType: 'json'
    })

        .done(function (data) {
            // log data to the console so we can see
            //console.log(data);
            console.log(data.top5);
            console.log(data.nearest);
            var cc = [];
            $.each(JSON.parse(data.top5), function (i, item) {
                var html = aVol + item.userName + bVol + cVol + item.userId + dVol;
                userIdVsInfoMap[item.userId] = item;
                cc.push(html);
            });
            $('#top_five_vol').html(cc.join(""));

            console.info(cc);

            var cc = [];
            $.each(JSON.parse(data.nearest), function (i, item) {
                var html = aVol + item.userName + bVol + cVol + item.userId + dVol;
                userIdVsInfoMap[item.userId] = item;
                cc.push(html);
            });
            $('#nearest_5_vol').html(cc.join(""));
            console.info(cc);


        });
    event.preventDefault();
}

function assignCaseReq(caseId, userId) {
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
            console.info("vkj");
            console.info(data);
            $('#assign-case-success').text("Success! Case " + caseId + " assigned successfully to " + userId);
            setTimeout(function(){
                $('#assign-case-success').fadeIn(200);
                $("#content-assign-case").css("pointerEvents", "none");}
            ,300)            
            //alert('Success div will show on server response');
            setTimeout(function () {
                navigate_back();
                navigate_back();
            }, 1200);
        });
    //event.preventDefault();
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
        dataType: 'json'
    })
        .done(function (data) {
            navigate_back();
            navigate_back();
        });
    event.preventDefault();
}




var responseHandler = function (event) {
    console.info("vkj : " + event.data);
    var data = event.data.split(":");
    var url = data[0];
    var tableId = data[1];
    console.info(" Clicked " + url + " " + tableId);
    $.ajax({
        type: 'GET', // define the type of HTTP verb we want to use (POST for our form)
        url: url, // the url where we want to POST
        dataType: 'json' // what type of data do we expect back from the server
        //encode: true
    })
        .done(function (data) {
                // log data to the console so we can see
                console.log(data);
                var cc = [];
                $.each(data, function (i, item) {
                    caseIdVsInfoMap[item.caseId] = item;
                    var htm = aCase + item.caseId + bCase + (item.active ? "Active" : "Closed") + cCase + item.creationDateStr + dCase + item.typeAnimal + eCase + item.animalName + fCase;
                    cc.push(htm);
                });

                $('#' + tableId).html(cc.join(""));
            }
        );
};

function enlargePhoto(this_t) {
    $('#dynamic_image_enlarge').css('display', 'block');
    var enlarge_source = this_t.getAttribute('src');
    $('#photo-enlarge').children('img').attr('src', enlarge_source);
    $('#photo-enlarge').children('img').css("background-color", "#2D3047");
}

$(document).ready(function () {

    $('.top-five-and-nearest-and-search-results-ul').on("mouseover", ".vol_tooltip", show_vol_tooltip);
    $('.top-five-and-nearest-and-search-results-ul').on("mouseout", ".vol_tooltip", hide_vol_tooltip);
    $('#myc-body').click('/activeCases:table1', responseHandler);
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
        this.classList.add('active-header');
        document.getElementById('recent-tab-header').classList.remove('active-header');
        document.getElementById('closed-tab-header').classList.remove('active-header');
        document.getElementById('recent-tab-header').classList.remove('tab-header-animation');
        document.getElementById('closed-tab-header').classList.remove('tab-header-animation');
        document.getElementById('active-tab-header').classList.add('tab-header-animation');
        document.getElementById('active-tab-content').classList.add('tab-content-animation');
        document.getElementById('recent-tab-content').classList.remove('tab-content-animation');
        document.getElementById('closed-tab-content').classList.remove('tab-content-animation');
        document.getElementById('active-tab-content').style.display = "block";
        document.getElementById('recent-tab-content').style.display = "none";
        document.getElementById('closed-tab-content').style.display = "none";
    };

    document.getElementById('recent-tab-header').onclick = function my_cases_search_results_recent() {
        this.classList.add('active-header');
        document.getElementById('active-tab-header').classList.remove('active-header');
        document.getElementById('closed-tab-header').classList.remove('active-header');
        document.getElementById('active-tab-header').classList.remove('tab-header-animation');
        document.getElementById('recent-tab-header').classList.add('tab-header-animation');
        document.getElementById('closed-tab-header').classList.remove('tab-header-animation');
        document.getElementById('active-tab-header').classList.remove('tab-content-animation');
        document.getElementById('active-tab-content').classList.remove('tab-content-animation');
        document.getElementById('recent-tab-content').classList.add('tab-content-animation');
        document.getElementById('recent-tab-content').style.display = "block";
        document.getElementById('closed-tab-content').style.display = "none";
        document.getElementById('active-tab-content').style.display = "none";
    };

    document.getElementById('closed-tab-header').onclick = function my_cases_search_results_closed() {
        this.classList.add('active-header');
        document.getElementById('active-tab-header').classList.remove('active-header');
        document.getElementById('recent-tab-header').classList.remove('active-header');
        document.getElementById('active-tab-header').classList.remove('tab-header-animation');
        document.getElementById('recent-tab-header').classList.remove('tab-header-animation');
        document.getElementById('closed-tab-header').classList.add('tab-header-animation');
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
//---------Close-Button-------------------------------------------------------
    document.getElementById("top-nav-search-close-button").onclick = function () {
        search_close_button();
    };


    document.getElementById("top-nav-search-user-profile-back-button").onclick = function () {
        navigate_back();
    };



//------Search-Active-Recent-Closed-Tabs-JS--------------------------------------
    document.getElementById('user-profile-active-tab-header').onclick = function user_profile_search_results_active() {
        this.classList.add('active-header');
        document.getElementById('user-profile-recent-tab-header').classList.remove('active-header');
        document.getElementById('user-profile-closed-tab-header').classList.remove('active-header');
        document.getElementById('user-profile-recent-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-closed-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-active-tab-header').classList.add('tab-header-animation');
        document.getElementById('user-profile-active-tab-content').classList.add('tab-content-animation');
        document.getElementById('user-profile-recent-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-closed-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-active-tab-content').style.display = "block";
        document.getElementById('user-profile-recent-tab-content').style.display = "none";
        document.getElementById('user-profile-closed-tab-content').style.display = "none";
    };

    document.getElementById('user-profile-recent-tab-header').onclick = function user_profile_search_results_recent() {
        this.classList.add('active-header');
        document.getElementById('user-profile-active-tab-header').classList.remove('active-header');
        document.getElementById('user-profile-closed-tab-header').classList.remove('active-header');
        document.getElementById('user-profile-active-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-recent-tab-header').classList.add('tab-header-animation');
        document.getElementById('user-profile-closed-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-active-tab-header').classList.remove('tab-content-animation');
        document.getElementById('user-profile-active-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-recent-tab-content').classList.add('tab-content-animation');
        document.getElementById('user-profile-recent-tab-content').style.display = "block";
        document.getElementById('user-profile-closed-tab-content').style.display = "none";
        document.getElementById('user-profile-active-tab-content').style.display = "none";
    };

    document.getElementById('user-profile-closed-tab-header').onclick = function user_profile_search_results_closed() {
        this.classList.add('active-header');
        document.getElementById('user-profile-active-tab-header').classList.remove('active-header');
        document.getElementById('user-profile-recent-tab-header').classList.remove('active-header');
        document.getElementById('user-profile-active-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-recent-tab-header').classList.remove('tab-header-animation');
        document.getElementById('user-profile-closed-tab-header').classList.add('tab-header-animation');
        document.getElementById('user-profile-active-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-recent-tab-content').classList.remove('tab-content-animation');
        document.getElementById('user-profile-closed-tab-content').classList.add('tab-content-animation');
        document.getElementById('user-profile-closed-tab-content').style.display = "block";
        document.getElementById('user-profile-active-tab-content').style.display = "none";
        document.getElementById('user-profile-recent-tab-content').style.display = "none";
    };
//------Basic-Details-and-User-Cases------------------------------------------------------------
    document.getElementById("user-profile-basic-details-btn").onclick = function () {
        user_profile_basic_details();
    };


    document.getElementById("user-profile-user-cases-btn").onclick = function user_profile_user_cases() {
        document.getElementById('user-profile-active-tab-header').classList.add('active-header');
        document.getElementById("user-profile-user-cases-btn").style.border = "2px solid #007AEA";
        document.getElementById("user-profile-basic-details-btn").style.border = "2px solid transparent";
        $("#user-profile-about-content").fadeOut(200);
        var user_profile_user_cases = setTimeout(function () {
            $("#user-profile-cases-content").fadeIn();
        }, 210);
    };

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
        }
    };
    window.onclick = function (e) {
        if (!e.target.matches('#more-btn')) {
            if (dropdown_menu_state === false) {
                document.getElementById("dropdown-menu").style.display = "none";
                $("main").css("opacity", "1");
                $("#card-right-side").css("opacity", "1");
                document.getElementsByTagName("body")[0].style.pointerEvents = "";
                document.getElementById("card-right-side").style.pointerEvents = "";
                dropdown_menu_state = !dropdown_menu_state;
            }
        }
    };

//------Bird-or-Animal-Affected-Changes-------------------------------------------------------------------------------------------------
    document.getElementById('bird-or-animal1').onclick = function () {  //Bird
        document.getElementById("animal-type").selectedIndex = 0;
        document.getElementById('header-form-left-side').innerHTML = "Bird Information";
        document.getElementById('animal-type-js').innerHTML = "Bird Type";
        document.getElementById('animal-name-js').innerHTML = "Bird Name";
        document.getElementById('animal-name').setAttribute("placeholder", "Eg: Cannary (Optional)");
        document.getElementById("raise-a-case-select-default").text = "Choose Bird Type:";
        $('#select-bird').css("display", "block");
        $('#select-animal').css("display", "none");
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
        $('#select-bird').css("display", "none");
        $('#select-animal').css("display", "block");
        $('#add-new-bird-animal').css("display", "block");
        $('#add-bird-animal').css("display", "none");
    };

    document.getElementById("animal-type").onchange = function(){
        if(document.getElementById("animal-type").value === "add-new-bird-animal"){
            document.getElementById("add-bird-animal").style.display = "block";
        }
        else {
            document.getElementById("add-bird-animal").style.display = "none";
        }
    };

    document.getElementById('reset-raise-case').onclick = function () {
        document.getElementById('header-form-left-side').innerHTML = "Bird/Animal Information";
        document.getElementById('animal-type-js').innerHTML = "Bird/Animal type: ";
        document.getElementById('animal-name-js').innerHTML = "Bird/Animal name: ";
        document.getElementById('animal-name').setAttribute("placeholder", "");
        $('#select-bird').css("display", "none");
        $('#select-animal').css("display", "none");
        $('#add-new-bird-animal').css("display", "none");
        $('#add-bird-animal').css("display", "none");
    };

//------Search-Focus-&-Clear-Table-on-Search---------------------------------------------------------------------------------------------
    document.getElementById('search-case-input').onkeyup = function () {
        if (document.getElementById('search-case-input').value !== "") {
            $('#tab-header').fadeOut(300);
            hideresults();
        } else {
            $('#tab-header').fadeIn(300);
            showresults();
        }
    };


//-----+91-022-Validation----------------------------------------------------------------------------------------------------------------
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


//------Case-Details---------------------------------------------------------------------------------------------------------------------


    document.getElementById("myc-case-details-heading-close-button").onclick = function () {
        document.getElementById('case-details').style.display = "none";
        document.getElementById('heading-case-details').style.display = "none";
        document.getElementById('case-details-form').style.display = "none";
        document.getElementById('top-nav-search-heading').style.display = "none";
        document.getElementById('top-nav-search-results').style.display = "none";
        document.getElementById('case-details').style.opacity = 1;
        document.getElementById('content-assign-case').style.display = "none";
        document.getElementById('content-close-case').style.display = "none";
        document.getElementById('case-details-form').style.pointerEvents = "";
        my_cases();
        page_history.unshift("my_cases");
    };

    document.getElementById("top-nav-case-details-heading-close-button").onclick = function () {
        search_close_button();
    };

    document.getElementById('top-nav-case-details-heading-back-button').onclick = function () {
        navigate_back();
    };
    document.getElementById('myc-case-details-heading-back-button').onclick = function () {
        navigate_back();
    };
//--Action-Centre--------------------------------------------------------------------------------
    document.getElementById('action-center-assign-case').onclick = function () {
        document.getElementById('case-details-form').style.display = "none";
        document.getElementById('content-assign-case').style.opacity = "1";
        document.getElementById('content-assign-case').style.display = "block";
        document.getElementById('myc-case-details-heading-animation').innerHTML = "#CaseID - Assign Case";
        document.getElementById("myc-case-details-heading-close-button").style.display = "block";
        page_history.unshift("action_centre_assign_case");
        getVolInfo();
    };

    document.getElementById('action-center-close-case').onclick = function () {
        document.getElementById("close-case-reason").selectedIndex = 0;
        document.getElementById('case-details-form').style.opacity = "0.2";
        document.getElementById('case-details-form').style.pointerEvents = "none";
        document.getElementById('content-close-case').style.opacity = "1";
        document.getElementById('content-close-case').style.display = "block";
        document.getElementById('myc-case-details-heading-animation').innerHTML = "#CaseID - Close Case";
        document.getElementById("myc-case-details-heading-close-button").style.display = "block";
        other_reason_close_case();
        page_history.unshift("action_centre_close_case");
    };

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


    document.getElementById('close-case-reason').onchange = function () {
        other_reason_close_case();
    };


//------Close-Case-Button--aka--Submit-Close-Case----------------------------------------------------------------------
    document.getElementById('close-case-submit-btn').onclick = function () {
        if (document.getElementById('close-case-reason').value === "Other" && document.getElementById('close-case-other-reason').value === "") {
            document.getElementById('close-case-other-reason').style.border = "2px solid red";
            document.getElementById('close-case-other-reason').placeholder = "You must specify a Reason";
        }
        else if (document.getElementById('close-case-reason').value === ""){
            document.getElementById('close-case-reason').style.border = "2px solid red";
        } 
        else {
            closeCaseReq(currentCaseId);
            // navigate_back();
            // navigate_back();
        }
    };
    document.getElementById('close-case-other-reason').addEventListener("keyup", function () {  //Removes Red border while typing
        document.getElementById('close-case-other-reason').style.border = "1px solid #666";
    });


//------Navigation-Back-Button----------------------------------------------------------------------------------------------------------


//------Keyboard-Shortcuts---------------------------------------------------------------------------------------------------------------
    document.onkeyup = function (e) {
        if (e.ctrlKey && e.altKey && e.which == 83) {
            if (document.getElementById('heading-raise-a-case').style.display == "" || document.getElementById('heading-raise-a-case').style.display == "block") {
                document.getElementById('submit-raise-case').click();
            } else alert("Ctrl + Alt + S is shortcut for Raising a Case");
        }
        if (e.ctrlKey && e.altKey && e.which == 82) {
            if (document.getElementById('heading-raise-a-case').style.display == "" || document.getElementById('heading-raise-a-case').style.display == "block") {
                document.getElementById('reset-raise-case').click();
            } else alert("Ctrl + Alt + R is shortcut for Resetting all fields of a Case");
        }
    };

//-----Disable-Alt+Left+Arrow----and------Disbale-Alt+Right+Arrow--------------------------------------------------------------
    document.onkeydown = function (e) {
        if (e.altKey && e.which == 37 || e.altKey && e.which == 39) {
            e.preventDefault();
        }
    };

//------Approve-Reject-Buttons-Effect--------------------------------------------------------------------------------------------------
    for (let i = 0; i < document.getElementsByClassName("approve").length; i++) {
        document.getElementsByClassName("approve")[i].onclick = function () {
            if (event.target.tagName == "IMG" && event.target.parentElement.parentElement.classList.contains('approve')) {
                event.target.setAttribute('src', '/static/img/approved-click.png');
                $(event.target.parentElement.parentElement.parentElement.parentElement).fadeOut();
            } else return false;
        }
    }

    for (let i = 0; i < document.getElementsByClassName("reject").length; i++) {
        document.getElementsByClassName("reject")[i].onclick = function () {
            if (event.target.tagName == "IMG" && event.target.parentElement.parentElement.classList.contains('reject')) {
                event.target.setAttribute('src', '/static/img/rejected-click.png');
                $(event.target.parentElement.parentElement.parentElement.parentElement).fadeOut();
            } else alert("Error");
        }
    }

//------Enlarge-Old-or-New-Photo-------------------------------------------------------------------------------------------------------
    $('.old-photo').children("figure").children("img").click(function () {
        enlargePhoto(this);
    });
//----User-Profile-DP-Enlarge-------------------------------------------------
    $('#user-profile-left-side').children('img').click(function () {
        enlargePhoto(this);
    });

    $('.new-photo').children("figure").children("img").click(function () {
        enlargePhoto(this);
    });

    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', 'none');
    });
//-----Disable-Right-Click-Menu-On-Image-----------------------------------------------------------------------------------------------
    $("body").on("contextmenu", "img", function (e) {
        return false;
    });

//-----Refresh-Button-Top-Nav----------------------------------------------------------------------------------------------------------
    document.getElementById("home-btn").onclick = function (e) {
        e.preventDefault();
        window.location.reload(true);
    };

//------Tooltip-Vol-Assign-Case--------------------------------------------------------------------------------------------------------

    document.getElementById("vol-description-tooltip").onmouseover = function (e) {
        clearTimeout(hide_tooltip_timeOut);
        $("#vol-description-tooltip").css("display", "block");
    };
    document.getElementById("vol-description-tooltip").onmouseout = function (e) {
        hide_vol_tooltip();
    };

//------BACKEND-INTEGRATION-------------------------------------------------------------------------------------------------------------


    // process the form
    $('#raise-a-case-form').submit(function (event) {
        var formData = {
            'typeAnimal': $('select[name=animal-type]').val(),
            'animalName': $('input[name=animal-name]').val(),
            'animalCondition': $('input[name=condition]').val(),
            'contactName': $('input[name=contact-name]').val(),
            'contactNumber': $('input[name=contact-number]').val(),
            'location': $('textarea[name=location]').val(),
            'locationPincode': $('input[name=location-pincode]').val(),
            'locationLandMark': $('input[name=location-landmark]').val(),
            'contactPrefix': $('select[name=nine-one]').val()
        };
        // process the form
        $.ajax({
            type: 'POST', // define the type of HTTP verb we want to use (POST for our form)
            url: '/addNewCase', // the url where we want to POST
            data: formData, // our data object
            dataType: 'json' // what type of data do we expect back from the server
            //encode: true
        })
        // using the done promise callback
            .done(function (data) {
                // log data to the console so we can see
                console.log(data);
                if (data && data == 'error') {
                    $('#error').text(data);
                }
                $('#raise-a-case-form')[0].reset();
                $('#case-id').val(data);
                setTimeout(function () {
                    $('#case-id').val('');
                }, 2000);
                // here we will handle errors and validation messages
            });

        // stop the form from submitting the normal way and refreshing the page
        event.preventDefault();
    });

    var container = $(document.createElement('div')).css({
        //padding: '5px', margin: '20px', width: '170px', border: '1px dashed',
        //borderTopColor: '#999', borderBottomColor: '#999',
        //borderLeftColor: '#999', borderRightColor: '#999'
    });
    $("#search-case-input").autocomplete({
        minLength: 1,
        delay: 500,
        //define callback to format results
        source: function (request, response) {
            $.getJSON("/getCaseInfoForSearch", request, function (result) {
                response($.map(result, function (item) {
                    return {
                        // following property gets displayed in drop down
                        label: item.caseId,
                        // following property gets entered in the textbox
                        value: item.caseId,
                        // following property is added for our own use

                        caseDetails: item
                    }

                }));
            });
            console.info("vkjk " + request + " " + response);
        },

        //define select handler
        select: function (event, ui) {
            var iCnt = 0;
            console.info(event);
            console.log(ui.item);
            if (ui.item) {
                caseDetails = ui.item.caseDetails;
                caseIdVsInfoMap[caseDetails.caseId] = caseDetails;
                iCnt = iCnt + 1;
                var htm = aCase + caseDetails.caseId + bCase + (caseDetails.active ? "Active" : "Closed") + cCase + caseDetails.creationDateStr + dCase + caseDetails.typeAnimal + eCase + caseDetails.animalName + fCase;
                $(container).append(htm);
                $('#table4').html(container);
                $("#search-case-input").autocomplete("close");
                $("#search-case-input").val('');
                console.info(container);
                return false;
            }
        }

    });

    $("#assign-case-search-input").autocomplete({
        minLength: 2,
        delay: 500,
        //define callback to format results
        source: function (request, response) {
            $("#assign_case_loading").css("display", "block");
            $.getJSON("/getVolListForSearch", request, function (result) {
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
            console.info("vkjk " + request + " " + response);
        },

        //define select handler
        select: function (event, ui) {
            console.info(event);
            console.log(ui.item);
            $("#assign_case_loading").css("display", "none");
            if (ui.item) {
                //var cc = [];
                var html = aVol + ui.item.userDetails.userName + bVol + cVol + ui.item.userDetails.userId + dVol;
                userIdVsInfoMap[ui.item.userDetails.userId] = ui.item.userDetails;
                //cc.push(html);
                $('#search_vol').html(html);
                //console.info(cc);
                $("#assign-case-search-input").autocomplete("close");
                $("#assign-case-search-input").val('');
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
                                // following property gets displayed in drop down
                                label: "Case : " + item.caseId,
                                // following property gets entered in the textbox
                                value: item.caseId,
                                // following property is added for our own use

                                caseDetails: item
                            }
                        } else {
                            return {
                                // following property gets displayed in drop down
                                label: "User : " + item.userName,
                                // following property gets entered in the textbox
                                value: item.userName,
                                // following property is added for our own use
                                userDetails: item
                            }
                        }

                    }));
                });
                // console.info("vkjk " + request + " " + response);
            },
        //define select handler
        select: function (event, ui) {
            console.log("VKJ : " + JSON.stringify(ui.item));
            if (ui.item) {
                document.getElementById('raise-a-case-content').style.opacity = 0;
                document.getElementById('my-cases-content').style.opacity = 0;
                document.getElementById('case-details').style.opacity = 0;
                document.getElementById('user-data-change-approvals-content').style.opacity = 0;
                document.getElementById('heading-raise-a-case').style.opacity = 0;
                document.getElementById('heading-my-cases').style.opacity = 0;
                document.getElementById('heading-case-details').style.opacity = 0;
                document.getElementById('heading-user-data-change-approvals').style.opacity = 0;
                document.getElementById('top-nav-search-heading').style.display = "block";
                document.getElementById("user-profile").style.display = "none";
                document.getElementById("top-nav-search-user-profile-back-button").style.display = "none";
                document.getElementById('heading-top-nav-case-details').style.display = "block";
                if (ui.item.userDetails) {
                    var ud = ui.item.userDetails;
                    var html = aVol + ud.userName + bVol + cVol + ud.userId + dVol;
                    userIdVsInfoMap[ud.userId] = ud;
                    $('#raise-a-case-content').hide();
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

                    top_nav_search_user_profile();
                    $('#user-profile').show();
                    //$('#search_vol').html(html);
                } else if (ui.item.caseDetails) {
                    cd = ui.item.caseDetails;
                    caseIdVsInfoMap[cd.caseId] = cd;
                    $('#raise-a-case-content').hide();

                    $('#case_profile_id').text(cd.caseId);
                    $('#case_profile_status').text(cd.active);
                    $('#case_profile_date').text(cd.creationDateStr);
                    $('#case_profile_type').text(cd.typeAnimal);
                    $('#case_profile_name').text(cd.animalName);

                    $('#case_profile').show();
                    top_nav_search_case_details("search_cases_details");
                    document.getElementById("top-nav-case-details-heading-back-button").style.display = "none";

                }
                /*document.getElementById('raise-a-case-content').style.opacity = 0;
                document.getElementById('my-cases-content').style.opacity = 0;
                document.getElementById('case-details').style.opacity = 0;
                document.getElementById('user-data-change-approvals-content').style.opacity = 0;
                document.getElementById('heading-raise-a-case').style.opacity = 0;
                document.getElementById('heading-my-cases').style.opacity = 0;
                document.getElementById('heading-case-details').style.opacity = 0;
                document.getElementById('heading-user-data-change-approvals').style.opacity = 0;
                document.getElementById('top-nav-search-heading').style.display = "block";
                document.getElementById("user-profile").style.display = "none";
                document.getElementById("top-nav-search-heading-content").innerHTML = "Searching results for - #Search_Input";
                document.getElementById("top-nav-search-user-profile-back-button").style.display = "none";
                page_history.unshift("top_nav_search_results");*/

                $("#top-nav-search").autocomplete("close");
                $("#top-nav-search").val(''); 
                return false;
            }
        }

    });

});

