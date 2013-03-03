package nu.wasis.blog;

import nu.wasis.blog.model.Post;
import nu.wasis.blog.model.User;
import nu.wasis.util.GPlusUtils;
import spark.Request;
import spark.Response;
import spark.Route;

final class SubmitPostRoute extends Route {
    SubmitPostRoute(String path) {
        super(path);
    }

    @Override
    public Object handle(final Request request, final Response response) {
        if (!GPlusUtils.isOwnerLoggedIn(request)) {
            response.status(403);
            return "You cannot do this.";
        }

        final String title = request.queryParams("title");
        final String body = request.queryParams("body");
        if (null == title || "".equals(title) || null == body || "".equals(body)) {
            response.status(400);
            return "Title or body missing.";
        }

        final User user = GPlusUtils.getCurrentUser(request);
        final Post post = new Post(title, body, user);
        Blog.postService.addPost(post);
        response.status(200);
        return "Post added.";
    }
}