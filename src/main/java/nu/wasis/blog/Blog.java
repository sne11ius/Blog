package nu.wasis.blog;

import nu.wasis.service.PostService;

import org.apache.log4j.Logger;

import spark.Spark;

public class Blog {

    static final Logger LOG = Logger.getLogger(Blog.class);
    static final PostService postService = new PostService();

    /**
     * @param args
     */
    public static void main(final String[] args) {
        // postService.deleteAllPosts();

        Spark.get(new IndexRoute("/"));
        Spark.post(new ConnectRoute("/connect"));
        Spark.post(new DisconnectRoute("/disconnect"));
        Spark.post(new SubmitPostRoute("/submitpost"));
    }
}
