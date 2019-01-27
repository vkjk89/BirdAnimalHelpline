function enlargePhoto(this_t) {
    $('#dynamic_image_enlarge').css('display', 'block');
    var enlarge_source = this_t.getAttribute('src');
    $('#photo-enlarge').children('img').attr('src', enlarge_source);
    $('#photo-enlarge').children('img').css("background-color", "rgba(255,255,255,0.6)");
    $('#top-nav').css("opacity", "0.5");
    $("#my-cases-case-details").css("opacity","0.1");
    $('#action-center-main-div').css("opacity","0.1");
    $('#bottom-action-bar').css("opacity","0.5");
    $("body").css("pointerEvents", "none");
    $('#image_enlarge_back_button').css("pointerEvents", "all");
}

$(document).ready(function () {
    caseImageRetriever($('#case-id-case-details').val(), "case-photos-case-details");
    $('#action-center-main-div').click(
        function () {
            window.location.href="updateCase?caseId="+$('#case-id-case-details').val();
        }
    );
    document.getElementById('profile-options').onclick = function openSideNav() {
        document.getElementById('side-nav').style.display = "block";
        document.getElementById('side-nav').classList.add('side-nav-anim');
        document.getElementById('top-nav').style.opacity = "0.1";
        $("#my-cases-case-details").css("opacity","0.1");
        $('#action-center-main-div').css("opacity","0.1");
        //$('body').css("border-left","3px solid #37404711");
    };

    document.getElementById('close-btn').onclick = function closeSideNav() {
        document.getElementById('side-nav').classList.remove('side-nav-anim');
        document.getElementById('side-nav').style.display = "none";
        document.getElementById('top-nav').style.opacity = "1";
        $("#my-cases-case-details").css("opacity","1");
        $('#action-center-main-div').css("opacity","1");
        //$('body').css("border-left","3px solid #374047");
    }; 

    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', 'none');
        document.getElementById('top-nav').style.opacity = "1";
        $("#my-cases-case-details").css("opacity","1");
        $('#action-center-main-div').css("opacity","1");
        $('#bottom-action-bar').css("opacity","1");
        $("body").css("pointerEvents", "all");
    });



    window.onclick = function (e) {
        if (!e.target.matches('#close-btn') && !e.target.matches('#profile-options') && !e.target.matches('#bottom-action-bar')) {
            if (document.getElementById('side-nav').classList.contains('side-nav-anim')) {
                document.getElementById('side-nav').classList.remove('side-nav-anim');
                document.getElementById('side-nav').style.display = "none";
                document.getElementById('top-nav').style.opacity = "1";
                $("#my-cases-case-details").css("opacity","1");
                $('#action-center-main-div').css("opacity","1");
                //$('body').css("border-left","3px solid #374047");
            }
        }
    };
});