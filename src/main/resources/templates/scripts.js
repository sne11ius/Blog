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

$('[type="button"].submit').click(function() {
    var body = $(this).closest('td').find('.commentbody').val(),
        id   = $(this).closest('.postcontainer').attr('id');
    $.post(window.location.href + 'posts/' + id + '/addcomment',
        {body:body},
        function() {
            window.location.reload();
        }
    );
});