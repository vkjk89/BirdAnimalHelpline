$(function(){
    var includes = $('[data-include]');
    jQuery.each(includes, function(){
      var file = '/templates/Vol_Dashboard/' + $(this).data('include') + '.html';
      $(this).load(file);
    });
});

$(document).ready(function (){
    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', 'none');
    });

    //BACKEND-INTEGRATION-----------------------------------------------------------------------
    ///JUST MAKE THE ARRAY - ILL PUT IT IN THE RIGHT PLACE-------------------------------


});