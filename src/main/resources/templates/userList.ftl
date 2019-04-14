<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
<h5>List of users</h5>

<table class="table table-striped">
    <thead class="thead-light">
        <tr>
            <th>Name</th>
            <#if isAnalyst>
                <th>Number of files</th>
            </#if>
            <#if isAdmin>
                <th>Confirmed</th>
                <th>Role</th>
                <th></th>
            </#if>
        </tr>
    </thead>
    <tbody>
    <#list users as user>
        <tr>
            <td><a href="/user-files/${user.id}">${user.username}</a></td>
            <#if isAnalyst>
                <td>${user.files?size}</td>
            </#if>
            <#if isAdmin>
                <td>${user.isConfirmed?string("yes", "no")}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/user/${user.id}">edit</a></td>
            </#if>
        </tr>
    </#list>
    </tbody>
</table>
</@c.page>
