<#include "security.ftl">

<table class="table table-striped">
    <thead class="thead-light">
        <tr>
            <th>File Name</th>
            <th>User Name</th>
            <#if isAnalyst>
                <th>Number of downloads</th>
            </#if>
            <th></th>
        </tr>
    </thead>
    <tbody>
        <#list files as file>
            <tr>
                <#if readAccess>
                    <td><b>${file.fileName}</b></td>
                <#else>
                    <td><a href="/download/${file.id}"><b>${file.fileName}</b></a></td>
                </#if>

                <td><a href="/user-files/${file.user.id}">${file.user.username}</a></td>

                <#if isAnalyst>
                    <td>${file.downloadCount}</td>
                </#if>

                <#if file.user.id == currentUserId || isAdmin>
                    <td><a class="btn btn-primary" href="/delete/${file.id}">Delete</a></td>
                <#else> <td> </td>
                </#if>
            </tr>
        </#list>
    </tbody>
</table>