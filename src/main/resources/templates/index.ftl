<!DOCTYPE html>
<html>
    <head>
        <title>wasis.nu/mit/blog</title>
    </head>
    <body>
        <#include "gplusheader.ftl"> 
        <h1>wasis.nu/mit/blog?</h1>
        <hr>
        <#if isowner>
            <#include "submitpostform.ftl">
        </#if>
        <#list posts as post>
            <#include "singlepost.ftl">
        </#list>
        <script type="text/javascript">
            <#include "gplushelper.js"> 
        </script>
    </body>
</html>