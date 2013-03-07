<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
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
            <#include "scripts.js">
        </script>
        <style>
            <#include "styles.css">
        </style>
        <div style="float:right">
        100% iso-8601 compliant
        </div>
    </body>
</html>
