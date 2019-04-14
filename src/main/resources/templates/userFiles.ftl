<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
<#if isFileOwner>
    <#include "parts/addFile.ftl" />
</#if>

<h3>File list of ${fileOwner.username}</h3>

<#if info??>
    <div class="alert alert-info">
        ${info?ifExists}
    </div>
</#if>

<#if !isAdminOrAnalystOrFileOwner>
    <a class="btn btn-info mb-2" href="/askread/${fileOwner.id}">Ask access to read</a>
    <a class="btn btn-info mb-2" href="/askdownload/${fileOwner.id}">Ask access to download</a>
</#if>

<#include "parts/fileList.ftl" />
</@c.page>