<script type="text/javascript">
    (function() {
        var po = document.createElement('script');
        po.type = 'text/javascript';
        po.async = true;
        po.src = 'https://plus.google.com/js/client:plusone.js';
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(po, s);
    })();
</script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<div style="float:right">
    <#if loggedin>
        <div>Logged in as ${nickname} <a href="javascript:helper.disconnectServer();" id="disconnect">[logout]</a></div>
    <#else>
        <div id="gConnect">
            <button class="g-signin"
                data-scope="https://www.googleapis.com/auth/plus.login"
                data-requestvisibleactions="http://schemas.google.com/AddActivity"
                data-clientId="${client_id}" data-accesstype="offline"
                data-callback="onSignInCallback" data-theme="dark"
                data-cookiepolicy="single_host_origin"></button>
        </div>
    </#if>
</div>
