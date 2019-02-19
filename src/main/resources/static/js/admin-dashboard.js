var usersPendingMap = new Map();

function accept_reject(accept_reject) {
    currentUserForApproval = data[slideIndex].userId;
    data.splice(slideIndex,1);
    acceptReject(accept_reject);
    if(data.length !== 0){
        slide_member(0);
        createBullets();
        $('.slider-member-count').html(data.length);
    } else no_user();
}

function acceptReject(accept_Reject) {
    var formData = {
        'userId': currentUserForApproval,
        'acceptReject': accept_Reject
    };
    $.ajax({
        type: 'POST',
        data: formData,
        url: '/enableUser'
    }).done(function (data) {
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
        slide_div.setAttribute("onclick","slide_member(this.innerHTML-currentSlide-1)");
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
    $(".slider-member-bullet").removeClass("case-slider-active-div");
    $(".slider-member-bullet-"+slideIndex).addClass("case-slider-active-div");
    $("#slider-wrapper").fadeOut(200);
    $("#slider-wrapper").fadeIn(200);
    setTimeout(function(){
    $(".user-accept-dp img").attr('src', "data:image/png;base64," + data[slideIndex].userImage.image);
    $(".user-role").html(data[slideIndex].role);
    $(".user-name").html(data[slideIndex].firstName + " " + data[slideIndex].lastName);
    $(".dob").html(data[slideIndex].dob);
    $(".mobile-number").html(data[slideIndex].mobile);
    $(".gender").html(data[slideIndex].gender);
    $(".email").html(data[slideIndex].email);
    },200);
    createBullets();
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

function no_user(){
    $('#slider-wrapper').fadeOut();
    $('#no-users-pending').fadeIn();
    $('.slider-member-count').text(data.length);
}

function getUsersListPendingForActivation() {
    $.ajax({
        type: 'GET',
        url: '/getUsersForActivation',
        //dataType: 'json'
    })
        .done(function (data) {
            if (data !== undefined) {
                $('.slider-member-count').html(data.length);
                if(data.length == 0) no_user();
                else {
                    $("#slider-wrapper").show();
                    $.each(data, function (i, item) {
                        if (!usersPendingMap.has(item.userId)) {
                            user_auth(0, item);
                            usersPendingMap.set(item.userId, item);
                        }
                        //createBullets();
                    });
                }
            }
            $("#loading-screen").css("display","none");
        });
}

function enlargePhoto(this_t) {
    $('#dynamic_image_enlarge').css('display', 'block');
    var enlarge_source = this_t.getAttribute('src');
    $('#photo-enlarge').children('img').attr('src', enlarge_source);
    $('#photo-enlarge').children('img').css("background-color", "#f4f4f4");
    $('#top-nav, #side-nav, main, #bottom-action-bar').css({'pointerEvents':'none', 'opacity':'0.5'});
}

$(document).ready(function (){
    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', 'none');
        $('#top-nav, #side-nav, main, #bottom-action-bar').css({'pointerEvents':'', 'opacity':'1'});
    });

    getUsersListPendingForActivation();
    setInterval(function () {
        getUsersListPendingForActivation();
    }, 12000);
    $("#logout").on("click", function (e) {
        e.preventDefault();
        window.location.href="/logout";
    });
    $("#home").on("click", function (e) {
        e.preventDefault();
        window.location.href="/default";
    });

    document.getElementById('profile-options').onclick = function openSideNav() {
        document.getElementById('side-nav').style.display = "block";
        document.getElementById('side-nav').classList.add('side-nav-anim');
        document.getElementById('top-nav').style.opacity = "0.1";
        document.getElementById('bottom-action-bar').style.opacity = "0.1";
        document.getElementsByTagName("main")[0].style.pointerEvents = "none";
        document.getElementsByTagName("main")[0].style.opacity = "0.1";
        document.getElementById("bottom-action-bar").style.pointerEvents = "none";
    };

    document.getElementById('close-btn').onclick = function closeSideNav() {
        document.getElementById('side-nav').classList.remove('side-nav-anim');
        document.getElementById('side-nav').style.display = "none";
        document.getElementById('top-nav').style.opacity = "1";
        document.getElementById('bottom-action-bar').style.opacity = "1";
        document.getElementsByTagName("main")[0].style.pointerEvents = "";
        document.getElementsByTagName("main")[0].style.opacity = "1";
        document.getElementById("bottom-action-bar").style.pointerEvents = "";
    };

    window.onclick = function (e) {
        if (!e.target.matches('#close-btn') && !e.target.matches('#profile-options') && !e.target.matches('#bottom-action-bar')) {
            if (document.getElementById('side-nav').classList.contains('side-nav-anim')) {
                document.getElementById('side-nav').classList.remove('side-nav-anim');
                document.getElementById('side-nav').style.display = "none";
                document.getElementById('top-nav').style.opacity = "1";
                document.getElementById('bottom-action-bar').style.opacity = "1";
                document.getElementsByTagName("main")[0].style.pointerEvents = "";
                document.getElementsByTagName("main")[0].style.opacity = "1";
                document.getElementById("bottom-action-bar").style.pointerEvents = "";
            }
        }
    };
});