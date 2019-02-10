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
                $('#' + imageDiv).html("");
                $.each(data, function (i, item) {
                    var image = document.createElement("img");
                    image.setAttribute("src", "data:image/png;base64," + item);
                    image.setAttribute("onclick", "enlargePhoto(this);");
                    $('#' + imageDiv).append(image);
                });
            }
        );
};

function enlargePhoto(this_t) {
    $('#dynamic_image_enlarge').css('display', 'block');
    var enlarge_source = this_t.getAttribute('src');
    $('#photo-enlarge').children('img').attr('src', enlarge_source);
    $('#photo-enlarge').children('img').css("background-color", "#2D3047");
}
