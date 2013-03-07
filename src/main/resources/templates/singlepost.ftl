<table class="postcontainer" id="${post.id}">
    <tr>
        <td class="label">
            <label for="post title ${post.id}">Title: </label>
        </td>
        <td>
            <h2 id="post title ${post.id}">${post.title}</h2>
        </td>
    </tr>
    <tr>
        <td class="label">
            <label for="post date ${post.id}">Date: </label>
        </td>
        <td>
            <#assign date = post.date?datetime>
            <span id="post date ${post.id}">${date?iso_utc}</span>
        </td>
    </tr>
    <tr>
        <td class="label">
            <label for="post body ${post.id}">Body: </label>
        </td>
        <td>
            <p id="post body ${post.id}">${post.body}</p>
        </td>
    </tr>
    <tr>
        <td class="label">
            <label for="post author ${post.id}">Author: </label>
        </td>
        <td>
            <span id="post author ${post.id}">${post.author.firstname} ${post.author.lastname}</span>
        </td>
    </tr>
    <#if loggedin>
        <tr>
            <td colspan="2">
                <table class="commentform">
                    <tr>
                        <td class="label">
                           <label for="commentbody ${post.id}">add comment</label>
                        </td>
                        <td>
                            <textarea class="commentbody" id="commentbody ${post.id}" cols="80" rows="4">comment body</textarea><br>
                        </td>
                    </tr>
                </table>
                <input type="button" value="submit comment" id="submitcomment" class="submit"></input>
            </td>
        </tr>
    </#if>
</table>

<hr>
