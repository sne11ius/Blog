package nu.wasis.blog;

import nu.wasis.blog.model.Post;
import nu.wasis.blog.model.User;
import nu.wasis.service.PostService;
import nu.wasis.util.GPlusUtils;

import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class Blog {

    static final Logger LOG = Logger.getLogger(Blog.class);
    static final PostService postService = new PostService();

    /**
     * @param args
     */
    public static void main(final String[] args) {
        postService.deleteAllPosts();

        Spark.get(new IndexRoute("/"));
        Spark.post(new ConnectRoute("/connect"));
        Spark.post(new DisconnectRoute("/disconnect"));

        Spark.post(new Route("/submitpost") {
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
                postService.addPost(post);
                response.status(200);
                return "Post added.";
            }
        });
    }
}
