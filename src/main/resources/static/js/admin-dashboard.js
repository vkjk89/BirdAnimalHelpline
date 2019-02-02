var usersPendingMap = new Map();

/*$(function(){
    var includes = $('[data-include]');
    jQuery.each(includes, function(){
      var file = '/templates/Vol_Dashboard/' + $(this).data('include') + '.html';
      $(this).load(file);
    });
});*/

function getUsersListPendingForActivation() {
    $.ajax({
        type: 'GET',
        url: '/getUsersForActivation',
        //dataType: 'json'
    })
        .done(function (data) {

                if (data) {
                    $('.slider-member-count').text(data.length);
                    $.each(data, function (i, item) {
                        console.info(item);
                        if (!usersPendingMap.has(item.userId)) {
                            // Make your changes here
                            // section_accept_case(item);
                            // slider_member_count = data.length;
                            // updateSliderMemeberCount = data.length;
                            // usersPendingMap.set(item.caseId, item);
                        }
                    });

                }
            }
        );
}

$(document).ready(function (){
    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', 'none');
    });

    //BACKEND-INTEGRATION-----------------------------------------------------------------------
    ///JUST MAKE THE ARRAY - ILL PUT IT IN THE RIGHT PLACE-------------------------------
    getUsersListPendingForActivation();
    setInterval(function () {
        getUsersListPendingForActivation();
    }, 30000);
    $("#logout").on("click", function (e) {
        e.preventDefault();
        window.location.href= "/logout";
    });

});