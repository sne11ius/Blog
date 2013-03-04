<label style="width:7em;display:inline-block;" for="post ${post.id} title">Title: </label><h2 id="post ${post.id} title" style="display:inline;">${post.title}</h2><br>
<label style="width:7em;display:inline-block;" for="post ${post.id} date">Date: </label><span id="post ${post.id} date" style="display:inline;">${post.date?datetime}</span><br>
<label style="width:7em;display:inline-block;" for="post ${post.id} body">Body: </label><p id="post ${post.id} body" style="display:inline;">${post.body}</p><br>
<label style="width:7em;display:inline-block;" for="post ${post.id} author">Author: </label><span id="post ${post.id} author" style="display:inline;">${post.author.firstname} ${post.author.lastname}</span>
<hr>
