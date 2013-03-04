<table>
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
            <span id="post date ${post.id}">${post.date?datetime}</span>
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
</table>

<hr>
