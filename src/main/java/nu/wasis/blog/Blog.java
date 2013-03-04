package nu.wasis.blog;

import nu.wasis.service.PostService;

import org.apache.log4j.Logger;

import spark.Spark;

public class Blog {

    static final Logger LOG = Logger.getLogger(Blog.class);
    static final PostService postService = new PostService();

    private static final String URL_PREFIX = "/mit/blog";

    /**
     * @param args
     */
    public static void main(final String[] args) {
        // postService.deleteAllPosts();

        Spark.get(new IndexRoute(URL_PREFIX + "/"));
        Spark.post(new ConnectRoute(URL_PREFIX + "/connect"));
        Spark.post(new DisconnectRoute(URL_PREFIX + "/disconnect"));
        Spark.post(new SubmitPostRoute(URL_PREFIX + "/submitpost"));
    }
}
