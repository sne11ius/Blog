package nu.wasis.blog;

import nu.wasis.blog.routes.AddCommentRoute;
import nu.wasis.blog.routes.ConnectRoute;
import nu.wasis.blog.routes.DisconnectRoute;
import nu.wasis.blog.routes.GetPostsRoute;
import nu.wasis.blog.routes.GetSinglePostRoute;
import nu.wasis.blog.routes.IndexRoute;
import nu.wasis.blog.routes.SubmitPostRoute;

import org.apache.log4j.Logger;

import spark.Spark;

public class Blog {

    static final Logger LOG = Logger.getLogger(Blog.class);
    private static final String URL_PREFIX = "/mit/blog";

    /**
     * @param args
     */
    public static void main(final String[] args) {
        // PostService.INSTANCE.deleteAllPosts();
        // return;
        Spark.get(new IndexRoute(URL_PREFIX + "/"));
        Spark.get(new GetPostsRoute(URL_PREFIX + "/posts"));
        Spark.get(new GetSinglePostRoute(URL_PREFIX + "/posts/:postId"));
        Spark.post(new AddCommentRoute(URL_PREFIX + "/posts/:postId/addcomment"));
        Spark.post(new ConnectRoute(URL_PREFIX + "/connect"));
        Spark.post(new DisconnectRoute(URL_PREFIX + "/disconnect"));
        Spark.post(new SubmitPostRoute(URL_PREFIX + "/submitpost"));
    }
}
