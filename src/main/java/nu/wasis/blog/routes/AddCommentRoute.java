package nu.wasis.blog.routes;

import nu.wasis.blog.model.Comment;
import nu.wasis.service.PostService;
import nu.wasis.util.GPlusUtils;
import spark.Request;
import spark.Response;
import spark.Route;

public class AddCommentRoute extends Route {

    public AddCommentRoute(final String path) {
        super(path);
    }

    @Override
    public Object handle(final Request request, final Response response) {
        if (!GPlusUtils.isOwnerLoggedIn(request)) {
            response.status(403);
            return "Not with you the force is.";
        }

        final String body = request.queryParams("body");
        final String postId = request.params("postId");
        if (null == body || "".equals(body) || null == postId || "".equals(postId)) {
            response.status(400);
            return "Id or Body missing.";
        }

        final Comment comment = new Comment(GPlusUtils.getCurrentUser(request), body);
        PostService.INSTANCE.addComment(postId, comment);

        response.status(200);
        return "Comment added.";
    }

}
