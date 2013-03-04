$('#submitpost').click(function() {
    var title = $('#posttitle').val(),
        body = $('#postbody').val();
    $.post(window.location.href + 'submitpost',
        {title:title,body:body},
        function() {
            window.location.reload();
        }
    );
});