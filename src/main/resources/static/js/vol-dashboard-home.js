function accept() {
    $('.celebrations').fadeIn();
    $(".accept-case-container").css("pointerEvents", "none");
    //window.scrollTo(0, document.body.scrollHeight);
}

function decline() {
    $('.celebrations').css("display", "none");
    $(".accept-case-container").css("pointerEvents", "none");
    //window.scrollTo(0, document.body.scrollHeight);
}

function acceptReject(caseId,acceptReject) {
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
                console.log("done vvkj : "+data);
            }
        );
}

function enlargePhoto(this_t) {
    $('#dynamic_image_enlarge').css('display', 'block');
    var enlarge_source = this_t.getAttribute('src');
    $('#photo-enlarge').children('img').attr('src', enlarge_source);
    $('#photo-enlarge').children('img').css("background-color", "#2D3047");
}

/*---------------------------Creating-Dynamic-Accept/Decline-Case-Members--------------------------------------*/
var slider_member_counter = 1;
var slider_member_bullet = 1;

function section_accept_case(data) {
    //Creating-and-Assigning-Attributes-----
    var slide_div = document.createElement("div");
        slide_div.classList.add("slider-member-bullet");
        slide_div.classList.add("slider-member-bullet-" + slider_member_bullet);
        slide_div.innerHTML = "l";

    var accept_case_container = document.createElement("div");
        accept_case_container.classList.add("accept-case-container");

    var case_details = document.createElement("div");
        case_details.classList.add("case-details");

    var box1 = document.createElement("div");
        box1.classList.add("box1");

    var text_case_id = document.createElement("span");
        text_case_id.innerHTML = "Case ID: ";

    var case_id = document.createElement("span");
        case_id.classList.add("case-id");
        case_id.innerHTML = data.caseId;

    var box2 = document.createElement("div");
        box2.classList.add("box2");

    var bird_animal_info_header = document.createElement("div");
        bird_animal_info_header.classList.add("bird-animal-info-header");
        bird_animal_info_header.innerHTML = "#Bird/Animal_Information";

    var animal_type = document.createElement("div");
        animal_type.classList.add("animal-type");
        animal_type.innerHTML = "#Bird/Animal_Type: " + data.typeAnimal;

    var animal_name = document.createElement("div");
        animal_name.classList.add("animal-name");
        if(data.animalName != ""){
            animal_name.innerHTML = "#Bird/Animal_Name: " + data.animalName;
        } else animal_name.innerHTML = "#Bird/Animal_Name: Unspecified";

    var text_span_condition = document.createElement("span");
        text_span_condition.innerHTML = "Condition: ";

    var case_condition = document.createElement("span");
        case_condition.classList.add("case-condition");
        if(data.animalCondition != ""){
        case_condition.innerHTML = data.animalCondition;
        } else case_condition.innerHTML = "Unspecified";

    var text_div_photos_uploaded = document.createElement("div");
        text_div_photos_uploaded.innerHTML = "Photos Uploaded: ";

    var case_photos = document.createElement("div");
        case_photos.classList.add("case-photos");
        case_photos.setAttribute("id","case-photos-"+data.caseId);
        //case_photos.id.add("case-photos");
        //Make innerHTML "none" by default, if photos are there then attach them.
        //case_photos.innerHTML = "<img src=\"\" onclick=\"enlargePhoto(this)\">";

    var box3 = document.createElement("div");
        box3.classList.add("box3");

    var contact_info_header = document.createElement("div");
        contact_info_header.classList.add("contact-info-header");
        contact_info_header.innerHTML = "Contact Information";

    var contact_info = document.createElement("div");
        contact_info.classList.add("contact-info");

    var text_span_name = document.createElement("span");
        text_span_name.innerHTML = "Name: ";

    var contact_name = document.createElement("span");
        contact_name.classList.add("contact-name");
        contact_name.innerHTML = data.contactName + "<br>";

    var text_span_number = document.createElement("span");
        text_span_number.innerHTML = "Contact Number: ";

    var contact_number = document.createElement("span");
        contact_number.classList.add("contact-number");
        contact_number.innerHTML = data.contactPrefix+"-"+data.contactNumber;

    var text_location = document.createElement("div");
        text_location.innerHTML = "Location: ";

    var contact_location = document.createElement("div");
        contact_location.classList.add("contact-location");
        if(data.location === ""){
            if(data.locationLandMark === ""){
            contact_location.innerHTML = data.locationPincode; 
            }
            else contact_location.innerHTML = data.locationLandMark+"<br>"+data.locationPincode;
        }else if(data.locationLandMark === ""){
            contact_location.innerHTML = data.locationPincode;
        }else {
        contact_location.innerHTML = data.location+"<br>"+data.locationLandMark+"<br>"+data.locationPincode;
        }

    var accept_decline = document.createElement("div");
        accept_decline.classList.add("accept-decline");

    var decline = document.createElement("button");
        decline.innerHTML = "I am helpless";
        decline.classList.add("decline");
        decline.setAttribute("type", "submit");
        decline.setAttribute("onclick", "decline("+data.caseId+");");
        decline.classList.add("slider-member-" + slider_member_counter);

    var accept = document.createElement("button");
        accept.innerHTML = "Accept";
        accept.classList.add("accept");
        accept.setAttribute("type", "submit");
        accept.setAttribute("onclick", "accept("+data.caseId+");");
        accept.classList.add("slider-member-" + slider_member_counter);

    var celebrations_container = document.createElement("div");
        celebrations_container.classList.add("celebrations-container");

    var celebrations = document.createElement("span");
        celebrations.classList.add("celebrations");

    var celebrations_image = document.createElement("img");
        celebrations_image.setAttribute("alt", "Case Animal/Bird");
        celebrations_image.setAttribute("th:src", "@{/img/pigeon.png}");        
        celebrations_image.setAttribute("width", "10vw");

    //Appending-Children-to-Parents
    //Box3---------------------------------------------------
    celebrations.appendChild(celebrations_image);
    celebrations_container.appendChild(celebrations);
    accept_decline.appendChild(decline);
    accept_decline.appendChild(accept);
    accept_decline.appendChild(celebrations_container);

    contact_info.appendChild(text_span_name);
    contact_info.appendChild(contact_name);
    contact_info.appendChild(text_span_number);
    contact_info.appendChild(contact_number);
    contact_info.appendChild(text_location);
    contact_info.appendChild(contact_location);

    box3.appendChild(contact_info_header);
    box3.appendChild(contact_info);
    box3.appendChild(accept_decline);
    //--------------------------------------------------------

    //Box2----------------------------------------------------
    box2.appendChild(bird_animal_info_header);
    box2.appendChild(animal_type);
    box2.appendChild(animal_name);
    box2.appendChild(text_span_condition);
    box2.appendChild(case_condition);
    box2.appendChild(text_div_photos_uploaded);
    box2.appendChild(case_photos);
    //--------------------------------------------------------

    //Box1----------------------------------------------------
    box1.appendChild(text_case_id);
    box1.appendChild(case_id);

    //Case-Details--------------------------------------------
    case_details.appendChild(box1);
    case_details.appendChild(box2);
    case_details.appendChild(box3);
    //--------------------------------------------------------

    //Accept-Case-Container-----------------------------------
    accept_case_container.classList.add("slider-member-count");
    accept_case_container.classList.add("slider-member-" + slider_member_counter);
    accept_case_container.appendChild(case_details);
    slider_member_counter++;
    slider_member_bullet++;
    //--------------------------------------------------------

    //Accept-Case---------------------------------------------
    document.getElementById("accept-case-wrapper").appendChild(accept_case_container);
    document.getElementById("accept-case-empty").style.display = "none";
    $(".accept-case-wrapper").css("minHeight", "85vh");
    document.getElementById("case-slider").style.display = "flex";
    document.getElementById("case-slider").appendChild(slide_div);
    $(".slider-nav-left").css("display", "inline-block");
    $(".slider-nav-right").css("display", "inline-block");
    slide_accept_case(0);
    //--------------------------------------------------------
}

//SLIDER-----------------------------------------------------------------------------------------------------
var currentSlide = 1;

function slide_accept_case(slide_direction) {
    var slider_member_count = document.getElementsByClassName("slider-member-count").length;
    var slideIndex = currentSlide + slide_direction;
    if (slideIndex < 1) {
        slideIndex = slider_member_count;
        //console.log("slider-member-" + (slideIndex) );
        //console.log( document.getElementsByClassName("slider-member-" + (slideIndex - 1) ) );
        $(".slider-member-count").css("display", "none");
        $(".slider-member-" + slideIndex).fadeIn(300);
        $(".slider-member-bullet").removeClass("case-slider-active-div");
        $(".slider-member-bullet-" + slideIndex).addClass("case-slider-active-div");
    } else if (slideIndex > 1 && slideIndex <= slider_member_count) {
        if (slide_direction === 1) {
            //console.log( document.getElementsByClassName("slider-member-" + (slideIndex - 1) ) );
            $(".slider-member-" + (slideIndex - 1)).css("display", "none");
            $(".slider-member-" + slideIndex).fadeIn(300);
            $(".slider-member-bullet").removeClass("case-slider-active-div");
            $(".slider-member-bullet-" + slideIndex).addClass("case-slider-active-div");
        } else {
            $(".slider-member-" + (slideIndex + 1)).css("display", "none");
            $(".slider-member-" + slideIndex).fadeIn(300);
            $(".slider-member-bullet").removeClass("case-slider-active-div");
            $(".slider-member-bullet-" + slideIndex).addClass("case-slider-active-div");
        }
    } else if (slideIndex === 1) {
        $(".slider-member-count").css("display", "none");
        $(".slider-member-1").fadeIn(300);
        $(".slider-member-bullet").removeClass("case-slider-active-div");
        $(".slider-member-bullet-" + slideIndex).addClass("case-slider-active-div");
        //console.log("slider-member-" + (slideIndex) );
        //console.log( document.getElementsByClassName("slider-member-" + (slider_member_count) ) );
        //setTimeout(function(){$(".slider-member-").fadeOut(500);}, 550);
    } else if (slideIndex > slider_member_count) {
        slideIndex = 1;
        //console.log("slider-member-" + (slideIndex) );
        //console.log( document.getElementsByClassName("slider-member-" + (slideIndex - 1) ) );
        $(".slider-member-count").css("display", "none");
        $(".slider-member-" + slideIndex).fadeIn(300);
        $(".slider-member-bullet").removeClass("case-slider-active-div");
        $(".slider-member-bullet-" + slideIndex).addClass("case-slider-active-div");
    }
    currentSlide = slideIndex;
}

//-----------------------------------------------------------------------------------------------------------

function displayPendingCases() {
    $.ajax({
        type: 'GET',
        // data: formData,
        url: '/getPendingCases'
        // dataType: 'json'
    })
        .done(function (data) {
                $.each(data, function (i, item) {
                    //caseIdVsInfoMap[item.caseId] = item;
                    //var htm = aCase + item.caseId + bCase + (item.userNameCurrent ? item.userNameCurrent + "(" + item.userRoleCurrent + ")" : "Closed") + cCase + item.creationDateStr + dCase + item.typeAnimal + eCase + item.animalName + fCase;
                    //cc.push(htm);
                    console.info(item);
                    section_accept_case(item);
                    caseImageRetriever(item.caseId,"case-photos-"+item.caseId);
                });

                //$('#' + tableId).html(cc.join(""));
            }
        );
}

//-----------------------------------------------------------------------------------------------------------

$(document).ready(function () {
    $("#logout").on("click", function (e) {
        e.preventDefault();
        window.location.assign("/logout");
        // $.ajax({
        //     url: "/logout",
        //     method : "GET"
        //     }
        // )
    });
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

    document.getElementById('search-case-input').onkeyup = function(){
        //Actually-put-this-part-in-success-or-done-ajax
        if(this.value.length > 1){
            $("#search-results-div").css("display", "none");
            $("#search-input-results").css("display", "block");
        }
        else {
            $("#search-results-div").css("display", "block");
            $("#search-input-results").css("display", "none");
        }
        
    };
    
    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', 'none');
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

});

//---------BACKEND-INTEGRATION----------------------------------------------------------------------------------------
$(document).ready(function () {

    var responseHandler = function (url, tableId) {
        console.info(" clicked " + url + " " + tableId);
        $.ajax({
            type: 'GET', // define the type of HTTP verb we want to use (POST for our form)
            url: url, // the url where we want to POST
            dataType: 'json' // what type of data do we expect back from the server
            //encode: true
        }).done(function (data) {
                var a = '<div><span class="case-id">';
                var b = '</span>';
                var c = '<span class="date">';
                var d = '</span><span class="animal-type">';
                var e = '</span>';
                var f = '</div>';
                // log data to the console so we can see
                var cc = [];
                $.each(data, function (i, item) {
                    var htm;
                    htm = a + item.caseId + b + c + item.creationDateStr + d + item.typeAnimal + e + f;
                    cc.push(htm);
                });
                $('#' + tableId).html(cc.join(""));
                if(cc.length === 0){
                    $('#' + tableId).html("<li style='z-index: 1000000001;background-color: rgb(250,250,250)'>No Cases</li>");  //DISPLAY-No-Cases-on-Tab-that-has-none
                }
            }
        );
    };

    displayPendingCases();
    $("#logout").on("click", function (e) {
        e.preventDefault();
        window.location.assign("/logout");
    });


    $('.case-id').click(function (e) {
        $("#dialog").dialog();
    });
    $('#results-tab-active').click(responseHandler('/activeCases', 'results-content-active'));
    $('#results-tab-active').click(responseHandler('/activeCases', 'results-content-active'));
    $('#results-tab-recent').click(responseHandler('/recentCases', 'results-content-recent'));
    $('#results-tab-closed').click(responseHandler('/closedCases', 'results-content-closed'));
});