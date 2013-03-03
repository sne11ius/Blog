<!DOCTYPE html>
<html>
    <head>
        <title>wasis.nu/mit/blog</title>
    </head>
    <body>
        <#include "gplusheader.ftl"> 
        <h1>wasis.nu/mit/blog?</h1>
        <#if isowner>
            <#include "submitpostform.ftl">
        </#if>
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