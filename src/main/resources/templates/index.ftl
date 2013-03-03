<html>
    <head>
        <title>wasis.nu/mit/blog</title>
    </head>
    <body>
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
            <div id="gConnect">
                <button class="g-signin"
                    data-scope="https://www.googleapis.com/auth/plus.login"
                    data-requestvisibleactions="http://schemas.google.com/AddActivity"
                    data-clientId="${client_id}" data-accesstype="offline"
                    data-callback="onSignInCallback" data-theme="dark"
                    data-cookiepolicy="single_host_origin"></button>
            </div>
        </div>
        <h1>wasis.nu/mit/blog?</h1>
            <div id="authOps" style="display: none">
            <h2>User is now signed in to the app using Google+</h2>
            <p>If the user chooses to disconnect, the app must delete all stored information retrieved from Google for the given user.</p>
            <button id="disconnect">Disconnect your Google account from this app</button>
    
            <h2>User's profile information</h2>
            <p>This data is retrieved client-side by using the Google JavaScript API client library.</p>
            <div id="profile"></div>
    
            <h2>User's friends that are visible to this app</h2>
            <p>This data is retrieved from your server, where your server makes an authorized HTTP request on the user's behalf.</p>
            <p>If your app uses server-side rendering, this is the section you would change using your server-side templating system.</p>
            <div id="visiblePeople"></div>
    
            <h2>Authentication Logs</h2>
            <pre id="authResult"></pre>
        </div>
        <#list posts as post>
            <div>
                <h2>${post.title?html}</h2>
                <p>${post.body?html}</p
            </div>
        </#list>
        <script type="text/javascript">
            <#include "gplushelper.js"> 
        </script>
    </body>
</html>