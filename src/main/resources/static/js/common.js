function compress(file) {
    return new Promise((resolve, reject) => {
        new Compressor(file, {
            quality : 0.3,
            success: resolve,
            error: reject,
        });
    })
}

var caseImageRetriever = function (caseId, imageDiv) {
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
                    $('#' + imageDiv).empty();
                    $('#' + imageDiv).html("No Photos");
                } else {
                    if(imageDiv === "case-photos-case-details") $("#myc_case_details_loading_screen").css("display","none");
                    else if(imageDiv === "case-photos") $("#vol_dashboard_accept_case_photos").css("display","none");
                    else if (imageDiv === "top-nav-contact-name-case-details") $("#top-nav-case-details-loading-screen").css("display","none");
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
