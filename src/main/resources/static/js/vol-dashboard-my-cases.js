function enlargePhoto(this_t) {
    $('#dynamic_image_enlarge').css('display', 'block');
    var enlarge_source = this_t.getAttribute('src');
    $('#photo-enlarge').children('img').attr('src', enlarge_source);
    $('#photo-enlarge').children('img').css("background-color", "rgba(255,255,255,0.6)");
    $('#top-nav').css("opacity", "0.5");
    $("#my-cases-case-details").css("opacity", "0.1");
    $('#action-center-main-div').css("opacity", "0.1");
    $('#action-center-main-history-div').css("opacity", "0.1");
    $('#bottom-action-bar').css("opacity", "0.5");
    $("body").css("pointerEvents", "none");
    $('#image_enlarge_back_button').css("pointerEvents", "all");
}

$(document).ready(function () {
    $("#logout").on("click", function (e) {
        e.preventDefault();
        window.location.href="/logout";
    });
    $("#home").on("click", function (e) {
        e.preventDefault();
        window.location.href="/";
    });
    caseImageRetriever($('#case-id-case-details').val(), "case-photos-case-details");
    $('#action-center-main-div').click(
        function () {
            window.location.href = "updateCase?caseId=" + $('#case-id-case-details').val();
        }
    );

    $('#action-center-main-history-div').click( function () {
        var formData = {
            'caseId': $('#case-id-case-details').val()
        };
        var a = '<tr>'
        var b = '<td class="tg-0lax">';
        var c = '</td>';
        var d = '</tr>';
        var tableHeader = '<tr><th class="tg-baqh">Sr</th><th class="tg-0lax">From User</th><th class="tg-0lax">To User</th><th class="tg-0lax">Ack</th><th class="tg-0lax">Amount</th><th class="tg-0lax">Description</th><th class="tg-0lax">Transfer Date</th></tr>';
        $.ajax({
            type: 'GET',
            url: '/getCaseTxn',
            data: formData,
            dataType: 'json'
        })
            .done(function (data) {
                $('#history-dialog-table').html('');
                var cc = [];
                cc.push(tableHeader);
                $.each(data, function (i, item) {
                    var html = a+b+(i+1)+c;
                    html += b+item.fromUser+'('+item.fromUserRole+')'+c;
                    html += b+item.toUser+'('+item.toUserRole+')'+c;
                    html += b+(item.isAck == 1? "Yes": item.isAck == -1 ? "No":"Pending")+c;
                    html += b+item.amount+c;
                    html += b+item.desc+c;
                    html += b+item.transferDate+c;
                    html += d;
                    cc.push(html);
                });
                $('#history-dialog-table').html(cc.join(""));
            });
        //$('#history-dialog').show();
        $("#history-dialog").dialog({
            width: 100,  height: 300
        });
    });
    document.getElementById('profile-options').onclick = function openSideNav() {
        document.getElementById('side-nav').style.display = "block";
        document.getElementById('side-nav').classList.add('side-nav-anim');
        document.getElementById('top-nav').style.opacity = "0.1";
        $("#my-cases-case-details").css("opacity", "0.1");
        $('#action-center-main-div').css("opacity", "0.1");
        $('#action-center-main-history-div').css("opacity", "0.1");
        //$('body').css("border-left","3px solid #37404711");
    };

    document.getElementById('close-btn').onclick = function closeSideNav() {
        document.getElementById('side-nav').classList.remove('side-nav-anim');
        document.getElementById('side-nav').style.display = "none";
        document.getElementById('top-nav').style.opacity = "1";
        $("#my-cases-case-details").css("opacity", "1");
        $('#action-center-main-div').css("opacity", "1");
        $('#action-center-main-history-div').css("opacity", "1");
        //$('body').css("border-left","3px solid #374047");
    };

    $('#image_enlarge_back_button').click(function () {
        $('#dynamic_image_enlarge').css('display', 'none');
        document.getElementById('top-nav').style.opacity = "1";
        $("#my-cases-case-details").css("opacity", "1");
        $('#action-center-main-div').css("opacity", "1");
        $('#action-center-main-history-div').css("opacity", "1");
        $('#bottom-action-bar').css("opacity", "1");
        $("body").css("pointerEvents", "all");
    });

    window.onclick = function (e) {
        if (!e.target.matches('#close-btn') && !e.target.matches('#profile-options') && !e.target.matches('#bottom-action-bar')) {
            if (document.getElementById('side-nav').classList.contains('side-nav-anim')) {
                document.getElementById('side-nav').classList.remove('side-nav-anim');
                document.getElementById('side-nav').style.display = "none";
                document.getElementById('top-nav').style.opacity = "1";
                $("#my-cases-case-details").css("opacity", "1");
                $('#action-center-main-div').css("opacity", "1");
                $('#action-center-main-history-div').css("opacity", "1");
                //$('body').css("border-left","3px solid #374047");
            }
        }
    };
});