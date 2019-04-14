<h5>Add file</h5>
<div>
    <form method="post" enctype="multipart/form-data">
        <input type="file" name="file">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="hidden" name="id" value="<#if file??>${file.id}</#if>" />
        <button type="submit">Add</button>
    </form>
</div>