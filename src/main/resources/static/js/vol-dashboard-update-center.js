var search_results_counter = 1;

$(document).ready(function () {
    $("#logout").on("click", function (e) {
        e.preventDefault();
        window.location.href="/logout";
    });
    $("#home").on("click", function (e) {
        e.preventDefault();
        window.location.href="/";
    });

    $('#notification_bell').click(function(){
        $('#notification-center').css({"opacity":"1", "pointerEvents":""});
        $('#notification-center').fadeToggle(300);
    });

    $("#search-input-assign-case").autocomplete({
        minLength: 1,
        delay: 500,
        source: function (request, response) {
            $.getJSON("/getVolListForSearch", request, function (result) {
                response($.map(result, function (item) {
                    return {
                        label: item.userName,
                        value: item.userId,
                        userDetails: item
                    }
                }));
            });
        },

        select: function (event, ui) {
            //$("#assign_case_loading").css("display", "none");
            if (ui.item) {
                console.info(ui.item);
                $('#assign-case-search-results').html('');
                $('#assign-case-search-results').html("<span>" + ui.item.label + "</span><input type='hidden' name='assignedUserId' value='" + ui.item.value + "'>");
                $("#search-input-assign-case").autocomplete("close");
                $("#search-input-assign-case").val('');
                return false;
            }
        }
    });

    document.getElementById('profile-options').onclick = function openSideNav() {
        document.getElementById('side-nav').style.display = "block";
        document.getElementById('side-nav').classList.add('side-nav-anim');
        document.getElementById('top-nav').style.opacity = "0.1";
        $("main").css({"opacity": "0.1", "pointerEvents": "none"});
        document.getElementById("notification-center").style.opacity = "0.1";
        document.getElementById("notification-center").style.pointerEvents = "none";
    };

    document.getElementById('close-btn').onclick = function closeSideNav() {
        document.getElementById('side-nav').classList.remove('side-nav-anim');
        document.getElementById('side-nav').style.display = "none";
        document.getElementById('top-nav').style.opacity = "1";
        $("main").css({"opacity": "1", "pointerEvents": ""});
        document.getElementById("notification-center").style.opacity = "1";
        document.getElementById("notification-center").style.pointerEvents = "";
    };

    window.onclick = function (e) {
        if (!e.target.matches('#close-btn') && !e.target.matches('#profile-options') && !e.target.matches('#bottom-action-bar')) {
            if (document.getElementById('side-nav').classList.contains('side-nav-anim')) {
                document.getElementById('side-nav').classList.remove('side-nav-anim');
                document.getElementById('side-nav').style.display = "none";
                document.getElementById('top-nav').style.opacity = "1";
                $("main").css({"opacity": "1", "pointerEvents": ""});
            }
        }
        if(!e.target.matches('#notification_bell') && document.getElementById('notification-center').style.display === "block"){
            $('#notification-center').css({"opacity":"1", "pointerEvents":""});
            $('#notification-center').fadeToggle(300);
        }
    };

    var now = new Date();
    var day = ("0" + now.getDate()).slice(-2);
    var month = ("0" + (now.getMonth() + 1)).slice(-2);
    var today = now.getFullYear() + "-" + (month) + "-" + (day);
    var min = now.getFullYear()-1 + "-" + (month) + "-" + (day);
    $('#update-case-date').val(today);
    $('#update-case-date').attr("max", today);
    $('#update-case-date').attr("min", min);

    $("#assign-case-toggle-btn").click(function () {
        $("#close-case-reason-wrapper").css("display", "none");
        $("#close-case-cta-btns").css("display", "none");
        $("#assign-case-wrapper").css("display", "block");
        $("#assign-case-cta-btns").css("display", "block");
        $("#assign-case-toggle-btn").addClass("active");
        $("#close-case-toggle-btn").removeClass("active");
    });

    $("#close-case-toggle-btn").click(function () {
        $('#assign-case-error').hide();
        $("#assign-case-wrapper").css("display", "none");
        $("#assign-case-cta-btns").css("display", "none");
        $("#close-case-reason-wrapper").css("display", "block");
        $("#close-case-cta-btns").css("display", "block");
        $("#assign-case-toggle-btn").removeClass("active");
        $("#close-case-toggle-btn").addClass("active");
    });

    $("#close-case-reason-select").on("change", function () {
        if ($("#close-case-reason-select").val() === "other") {
            $("#close-case-other-reason").css("display", "block");
            $("#close-case-other-reason").attr("required", "");
        } else {
            $("#close-case-other-reason").css("display", "none");
            $("#close-case-other-reason").removeAttr("required", "");
        }
    });

    $("#search-input-assign-case").on("keyup keypress keydown", function () {
        $('#assign-case-error').hide();
        if ($("#search-input-assign-case").val().length > 2) {
            $("#assign-case-search-results").css("display", "block");
        } else if ($("#search-input-assign-case").val().length === 0) $("#assign-case-search-results").css("display", "none");
    });

    $("#assign-case-assign").click(function (event) {
        event.preventDefault();
        if ($('#assign-case-search-results').html() == '') {
            $("#assign-case-error").css("display", "flex");
            return;
        } else {
            $("#assign-case-error").css("display", "none");
            $("#action").val("assign");
            $("#assign-case-submit-final").click();
        }
    });

    $("#assign-case-cancel").click(function (event) {
        event.preventDefault();
        window.location.href="/default";
    });

    $("#close-case-close").click(function (event) {
        event.preventDefault();
        $("#action").val("close");
        $("#assign-case-submit-final").click();
    });

    $("#close-case-cancel").click(function (event) {
        event.preventDefault();
        window.location.href="/default";
    });
    $("#loading-screen").hide();
});