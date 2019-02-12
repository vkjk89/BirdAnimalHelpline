var usersPendingCaseMap = new Map();

function accept_decline(accept_decline) {
    var caseId = data1[slideIndex].caseId;
    $(".slider-member-bullet-" + slideIndex).remove();
    data1.splice(slideIndex,1);
    if(data1.length !== 0){
        slide_accept_case(0);
        createBullets();
    } else no_pending_cases();
    $('.accept-decline-pending-cases').html(data1.length);
    acceptReject(caseId, accept_decline);
    if(accept_decline === true){
        $('.celebrations').fadeIn(200);
        setTimeout(function(){
            $('.celebrations-container').fadeOut(200);
        }, 200);
    }
}

function no_pending_cases(){
    $("#accept-case-empty").show();
    $(".accept-case-container").hide();
    $("#case-slider-bullets").hide();
    $(".accept-case-wrapper").css("marginBottom","3vh");
    $(".slider-navigation-buttons").hide();    
}

function acceptReject(caseId, acceptReject) {
    var formData = {
        'caseId': caseId,
        'acceptReject': acceptReject
    };
    $.ajax({
        type: 'POST',
        data: formData,
        url: '/acceptRejectCase'
        // dataType: 'json'
    })
        .done(function (data) {
                //console.log("done vvkj : " + data);
            }
        );
}

function enlargePhoto(this_t) {
    $('#dynamic_image_enlarge').css('display', 'block');
    var enlarge_source = this_t.getAttribute('src');
    $('#photo-enlarge').children('img').attr('src', enlarge_source);
    $('#top-nav').css({"opacity":"0.95", "pointerEvents":"none"});
    $('#page-heading').css({"opacity":"0.2", "pointerEvents":"none"});
    $('main').css({"opacity":"0.2", "pointerEvents":"none"});
    $('#bottom-action-bar').css({"opacity":"0.95", "pointerEvents":"none"});
}

/*---------------------------Creating-Dynamic-Accept/Decline-Case-Members--------------------------------------*/
var slider_member_counter = 0;
var slider_member_bullet = 0;

function createBullets(){
    $("#case-slider-bullets").empty();
    slider_member_bullet = 0;
    for(var i = 0; i < data1.length; i++){
        //CREATING-BULLETS-ACCEPT-CASE-HEADER
        var slide_div = document.createElement("div");
        slide_div.classList.add("slider-member-bullet");
        slide_div.classList.add("slider-member-bullet-" + slider_member_bullet);
        slide_div.innerHTML = slider_member_bullet+1;
        //APPENDING-ELEMENTS-------------------
        document.getElementById("case-slider-bullets").appendChild(slide_div);
        slider_member_bullet++;
    }
    $(".slider-member-bullet-"+slideIndex).addClass("case-slider-active-div");
}
var data1 = [];
function section_accept_case(slideIndex, item) {
    if(item != undefined){
        data1.push(item);
    }
    $(".slider-member-bullet").removeClass("case-slider-active-div");
    $(".slider-member-bullet-"+slideIndex).addClass("case-slider-active-div");
    setTimeout(function(){
        $(".case-id").html(data1[slideIndex].caseId);
        if(data1[slideIndex].birdOrAnimal === "Animal"){
            $(".bird-animal-info-header").html("Animal Information");
            $(".animal-type").html("Animal Type: " + data1[slideIndex].typeAnimal);
            if(data1[slideIndex].animalName) $(".animal-name").html("Animal Name: " + data1[slideIndex].animalName);
            else $(".animal-name").html("Animal Name: ");
            if(data1[slideIndex].animalCondition) $(".animalCondition").html(data1[slideIndex].animalCondition);
            else $(".animalCondition").html("");
        }else {
            $(".bird-animal-info-header").html("Bird Information");
            $(".animal-type").html("Bird Type: " + data1[slideIndex].typeAnimal);
            if(data1[slideIndex].animalName) $(".animal-name").html("Bird Name: " + data1[slideIndex].animalName);
            else $(".animal-name").html("Bird Name: ");
            if(data1[slideIndex].animalCondition) $(".animalCondition").html(data1[slideIndex].animalCondition);
            else $(".animalCondition").html("");
        }
        $(".contact-name").html(data1[slideIndex].contactName);
        $(".contact-number").html("<a href='tel:" +  data1[slideIndex].contactPrefix  + data1[slideIndex].contactNumber + "'>" + data1[slideIndex].contactPrefix + "-" + data1[slideIndex].contactNumber + "</a>");
        if(data1[slideIndex].locationLandMark){
            $(".contact-location").html(data1[slideIndex].location + "<br>" + data1[slideIndex].locationLandMark + "<br>" + data1[slideIndex].locationPincode);
        }
        else $(".contact-location").html(data1[slideIndex].location + "<br>" + data1[slideIndex].locationPincode);
    }, 200);
}

//SLIDER-----------------------------------------------------------------------------------------------------
var currentSlide = 0;
var slideIndex = 0;

function slide_accept_case(slide_direction) {
    slideIndex = currentSlide + slide_direction;
    $(".case-details").fadeOut(200);
    $(".case-details").fadeIn(200);
    if (slideIndex < 0) {
        slideIndex = data1.length-1;
        section_accept_case(slideIndex);
    } else if (slideIndex >= 0 && slideIndex < data1.length) {
        section_accept_case(slideIndex);
    } else if (slideIndex+1 >= data1.length) {
        slideIndex = 0;
        section_accept_case(slideIndex);
    }
    $("#case-photos").empty();
    caseImageRetriever(data1[slideIndex].caseId, "case-photos");
    currentSlide = slideIndex;
}

//-----------------------------------------------------------------------------------------------------------

function displayPendingCases() {
    $.ajax({
        type: 'GET',
        url: '/getPendingCases'
    })
        .done(function (data) {
                if (data !== undefined) {
                    $('.accept-decline-pending-cases').html(data.length);
                    if(data.length == 0) no_pending_cases();
                    else {
                        $("#case-slider-bullets").show();
                        $(".accept-case-wrapper").css("marginBottom","10vh");
                        $(".slider-navigation-buttons").show();

                        $.each(data, function (i, item) {
                            if (!usersPendingCaseMap.has(item.caseId)) {
                                section_accept_case(slideIndex, item);
                                createBullets();
                                usersPendingCaseMap.set(item.caseId, item);
                            }
                        });
                        slide_accept_case(0);
                    }
                }
            }
        );
}

function goToCaseDetails(element) {
    //console.info(element);
    var caseId = $(element).find(".case-id-my-cases").text();
    //console.info(caseId);
    window.location.href = "/caseDetails?caseId=" + caseId;
}

//-----------------------------------------------------------------------------------------------------------

$(document).ready(function () {
    displayPendingCases();
    setInterval(function () {
        displayPendingCases();
    }, 30000);
    document.getElementById('results-tab-active').onclick = function active_active() {
        this.classList.add('active-active');
        document.getElementById('results-tab-recent').classList.remove('active-recent');
        document.getElementById('results-tab-closed').classList.remove('active-closed');

        document.getElementById('results-content-active').style.display = "block";
        document.getElementById('results-content-recent').style.display = "none";
        document.getElementById('results-content-closed').style.display = "none";

        document.getElementById('results-content-active').classList.add('results-content-animation');
        document.getElementById('results-content-recent').classList.remove('results-content-animation');
        document.getElementById('results-content-closed').classList.remove('results-content-animation');
    };
    document.getElementById('results-tab-recent').onclick = function recent_active() {
        this.classList.add('active-recent');
        document.getElementById('results-tab-active').classList.remove('active-active');
        document.getElementById('results-tab-closed').classList.remove('active-closed');

        document.getElementById('results-content-recent').style.display = "block";
        document.getElementById('results-content-active').style.display = "none";
        document.getElementById('results-content-closed').style.display = "none";

        document.getElementById('results-content-active').classList.remove('results-content-animation');
        document.getElementById('results-content-recent').classList.add('results-content-animation');
        document.getElementById('results-content-closed').classList.remove('results-content-animation');
    };
    document.getElementById('results-tab-closed').onclick = function closed_active() {
        this.classList.add('active-closed');
        document.getElementById('results-tab-active').classList.remove('active-active');
        document.getElementById('results-tab-recent').classList.remove('active-recent');

        document.getElementById('results-content-closed').style.display = "block";
        document.getElementById('results-content-active').style.display = "none";
        document.getElementById('results-content-recent').style.display = "none";

        document.getElementById('results-content-active').classList.remove('results-content-animation');
        document.getElementById('results-content-recent').classList.remove('results-content-animation');
        document.getElementById('results-content-closed').classList.add('results-content-animation');
    };

    document.getElementById('profile-options').onclick = function openSideNav() {
        document.getElementById('side-nav').style.display = "block";
        document.getElementById('side-nav').classList.add('side-nav-anim');
        document.getElementById('top-nav').style.opacity = "0.1";
        document.getElementById('page-heading').style.opacity = "0.1";
        document.getElementById('my-cases').style.opacity = "0.1";
        document.getElementById('accept-case-wrapper').style.opacity = "0.1";
        document.getElementsByTagName("main")[0].style.pointerEvents = "none";
    };

    document.getElementById('close-btn').onclick = function closeSideNav() {
        document.getElementById('side-nav').classList.remove('side-nav-anim');
        document.getElementById('side-nav').style.display = "none";
        document.getElementById('page-heading').style.opacity = "1";
        document.getElementById('top-nav').style.opacity = "1";
        document.getElementById('my-cases').style.opacity = "1";
        document.getElementById('accept-case-wrapper').style.opacity = "1";
        document.getElementsByTagName("main")[0].style.pointerEvents = "";
    };

    document.getElementById('search-case-input').onkeyup = function () {
        //Actually-put-this-part-in-success-or-done-ajax
        if (this.value.length > 1) {
            $("#search-results-div").css("display", "none");
            $("#search-input-results").css("display", "block");
        } else {
            $("#search-results-div").css("display", "block");
            $("#search-input-results").css("display", "none");
        }

    };

    $("#logout").on("click", function (e) {
        e.preventDefault();
        window.location.href="/logout";
    });
    $("#home").on("click", function (e) {
        e.preventDefault();
        window.location.href="/default";
    });

    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', '');
        $('#top-nav').css({"opacity":"1", "pointerEvents":""});
        $('main').css({"opacity":"1", "pointerEvents":""});
        $('#bottom-action-bar').css({"opacity":"1", "pointerEvents":""});
        $('#page-heading').css({"opacity":"1", "pointerEvents":""});
    });

    window.onclick = function (e) {
        if (!e.target.matches('#close-btn') && !e.target.matches('#profile-options') && !e.target.matches('#bottom-action-bar')) {
            if (document.getElementById('side-nav').classList.contains('side-nav-anim')) {
                document.getElementById('side-nav').classList.remove('side-nav-anim');
                document.getElementById('side-nav').style.display = "none";
                document.getElementById('page-heading').style.opacity = "1";
                document.getElementById('top-nav').style.opacity = "1";
                document.getElementById('my-cases').style.opacity = "1";
                document.getElementById('accept-case-wrapper').style.opacity = "1";
                document.getElementsByTagName("main")[0].style.pointerEvents = "";
            }
        }
    };

    var aCase = '<div onclick="goToCaseDetails(this)"><span class="case-id-my-cases">';
    var bCase = '</span>';
    var cCase = '<span class="date-my-cases">';
    var dCase = '</span><span class="animal-type-my-cases">';
    var eCase = '</span>';
    var fCase = '</div>';

    var responseHandler = function (event) {
        var data = event.data.split(":");
        var url = data[0];
        var tableId = data[1];
        var userId = -1;
        if (data.length == 3) {
            userId = data[2];
        }
        var formData = {
            'userId': userId,
        };
        //console.info(" Clicked " + url + " " + tableId + " for user : " + userId);

        $.ajax({
            type: 'GET', // define the type of HTTP verb we want to use (POST for our form)
            url: url, // the url where we want to POST
            dataType: 'json' // what type of data do we expect back from the server
            //encode: true
        }).done(function (data) {
                // log data to the console so we can see
                //console.log(data);
                var cc = [];
                $.each(data, function (i, item) {
                    var htm = aCase + item.caseId + bCase + cCase + item.creationDateStr + dCase + item.typeAnimal + eCase + fCase;
                    //var htm = aCase + item.caseId + b + item.userNameCurrent + "(" + item.userRoleCurrent + ")" + c + item.creationDateStr + d + item.typeAnimal + e + item.animalName + f;
                    //var htm = aCase + item.caseId + bCase + cCase + item.creationDateStr + dCase + item.typeAnimal + eCase + fCase;
                    cc.push(htm);
                });
                $('#' + tableId).html(cc.join(""));
                if (cc.length === 0) {
                    //DISPLAY-No-Cases-on-Tab-that-has-none
                    $('#' + tableId).html("<li style='z-index: 1000000001;background-color: rgb(250,250,250)'>No Cases</li>");
                }
            }
        );
    };

    $('#results-tab-active').click('/activeCases:results-content-active', responseHandler);
    $('#results-tab-recent').click('/recentCases:results-content-recent', responseHandler);
    $('#results-tab-closed').click('/closedCases:results-content-closed', responseHandler);

    $('#results-tab-active').click();
});
