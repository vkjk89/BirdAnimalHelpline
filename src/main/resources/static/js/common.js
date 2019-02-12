var caseImageRetriever = function (caseId, imageDiv) {
    $('.' + imageDiv).html("");
    var formData = {
        'caseId': caseId
    };
    $.ajax({
        type: 'GET',
        data: formData,
        url: '/getCaseImages',
    })
        .done(function (data) {
                if(data === null || data === undefined || data.length === 0){
                    $('#' + imageDiv).html("No Photos");
                } else {
                    $('#' + imageDiv).empty();
                    $.each(data, function (i, item) {
                        var image = document.createElement("img");
                        image.setAttribute("src", "data:image/png;base64," + item);
                        image.setAttribute("onclick", "enlargePhoto(this);");
                        $('#' + imageDiv).append(image);
                    });
                }
            }
        );
};

$(function(){
    var includes = $('[data-include]');
    jQuery.each(includes, function(){
      var file = '/templates/Vol_Dashboard/' + $(this).data('include') + '.html';
      $(this).load(file);
    });
});

function enlargePhoto(this_t) {
    $('#dynamic_image_enlarge').css('display', 'block');
    var enlarge_source = this_t.getAttribute('src');
    $('#photo-enlarge').children('img').attr('src', enlarge_source);
    $('#photo-enlarge').children('img').css("background-color", "#2D3047");
}
