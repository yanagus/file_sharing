<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>

<@c.page>
<h3>List of my requesting subscribers</h3>

<#if message??>
    <div class="form-group row">
        <div class="col-sm-8">
            <div class="alert alert-danger">
                ${message?ifExists}
            </div>
        </div>
    </div>
</#if>

<table class="table table-striped">
    <thead class="thead-light">
        <tr>
            <th>User Name</th>
            <th>Request to read</th>
            <th>Request to download</th>
        </tr>
    </thead>
    <tbody>
        <#if accesses??>
            <#list accesses as access>
                <tr>
                    <td><a href="/user-files/${access.subscriber.id}">${access.subscriber.username}</a></td>

                    <#if access.readAccess>
                        <td><a class="btn btn-primary" href="/allowRead/${access.subscriber.id}">Allow</a></td>
                    <#else> <td> </td>
                    </#if>

                    <#if access.downloadAccess>
                        <td><a class="btn btn-primary" href="/allowDownload/${access.subscriber.id}">Allow</a></td>
                    <#else> <td> </td>
                    </#if>
                </tr>
            </#list>
        </#if>
    </tbody>
</table>

</@c.page>