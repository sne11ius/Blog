<div style="width:0%;">
    <input type="text" id="posttitle" size="50" maxlength="9001" value="post title"></input>
    <textarea id="postbody" cols="80" rows="20">post body</textarea><br>
    <input type="button" value="submit post" id="submitpost"></input>
</div>

<script>
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
</script>
