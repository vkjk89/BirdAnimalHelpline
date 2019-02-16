var usersPendingMap = new Map();

function accept_reject(accept_reject) {
    data.splice(slideIndex,1);
    var userId = data[slideIndex].userId;
    console.log(userId);
    acceptReject(userId, accept_reject);
    createBullets();
    slide_member(0);
}

function acceptReject(acceptReject) {
    var formData = {
        'userId': currentUserForApproval,
        'acceptReject': acceptReject
    };
    $.ajax({
        type: 'POST',
        data: formData,
        url: '/enableUser'
    })
        .done(function (data) {
                        console.log("done vvkj : " + data);
            }
        );
}

var slider_member_counter = 0;
var slider_member_bullet = 0;

function createBullets(){
    $("#case-slider-bullets").empty();
    slider_member_bullet = 0;
    for(var i = 0; i < data.length; i++){
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

var data = [];
function user_auth(slideIndex, item){
    if(item !== undefined){
        data.push(item);
    }
    console.log(data);
    $(".slider-member-bullet").removeClass("case-slider-active-div");
    $(".slider-member-bullet-"+slideIndex).addClass("case-slider-active-div");
    $("#slider-wrapper").fadeOut(200);
    $("#slider-wrapper").fadeIn(200);
    currentUserForApproval = data[slideIndex].userId;
    setTimeout(function(){
    $(".user-accept-dp img").attr('src', "data:image/png;base64," + data[slideIndex].userImage.image);
    $(".user-role").html(data[slideIndex].role);
    $(".user-name").html(data[slideIndex].firstName + " " + data[slideIndex].lastName);
    $(".dob").html(data[slideIndex].dob);
    $(".mobile-number").html(data[slideIndex].mobile);
    $(".gender").html(data[slideIndex].gender);
    $(".email").html(data[slideIndex].email);
    },200);
}

var currentSlide = 0;
var slideIndex = 0;

function slide_member(slide_direction) {
    slideIndex = currentSlide + slide_direction;
    if (slideIndex < 0) {
        slideIndex = data.length-1;
        user_auth(slideIndex);
    } else if (slideIndex >= 0 && slideIndex < data.length) {
        user_auth(slideIndex);
    } else if (slideIndex+1 >= data.length) {
        slideIndex = 0;
        user_auth(slideIndex);
    }
    currentSlide = slideIndex;
}

function getUsersListPendingForActivation() {
    $.ajax({
        type: 'GET',
        url: '/getUsersForActivation',
        //dataType: 'json'
    })
        .done(function (data) {
                console.log(data);
                console.log(data.length);
                if (data) {
                    if(data.length == 0) {
                        $('#slider-wrapper').hide();
                        $('#no-users-pending').fadeIn();
                    }
                    $('.slider-member-count').text(data.length);
                    $.each(data, function (i, item) {
                        //console.info(item);
                        if (!usersPendingMap.has(item.userId)) {
                            user_auth(0, item);
                            //console.log(item);
                            // Make your changes here
                            // section_accept_case(item);
                            // slider_member_count = data.length;
                            // updateSliderMemeberCount = data.length;
                            usersPendingMap.set(item.userId, item);
                        }
                        createBullets();
                    });

                }
            }
        );
}

$(document).ready(function (){
    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', 'none');
    });
    
    getUsersListPendingForActivation();
    setInterval(function () {
        getUsersListPendingForActivation();
    }, 12000);
    $("#logout").on("click", function (e) {
        e.preventDefault();
        window.location.href= "/logout";
    });

});