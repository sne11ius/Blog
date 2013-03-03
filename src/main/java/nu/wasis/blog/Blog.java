package nu.wasis.blog;

import static spark.Spark.get;
import static spark.Spark.post;
import nu.wasis.blog.model.Post;
import nu.wasis.service.PostService;

import org.apache.log4j.Logger;

import spark.Spark;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.Gson;

public class Blog {

    static final Logger LOG = Logger.getLogger(Blog.class);
    static PostService postService = new PostService();

    /**
     * Default HTTP transport to use to make HTTP requests.
     */
    static final HttpTransport TRANSPORT = new NetHttpTransport();

    /**
     * Default JSON factory to use to deserialize JSON.
     */
    static final JacksonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Gson object to serialize JSON responses to requests to this servlet.
     */
    static final Gson GSON = new Gson();

    /**
     * @param args
     */
    public static void main(final String[] args) {
        // final Post post = new Post();
        // post.setBody("post body ftw!");
        // post.setTitle("post title ftw!");
        // final User user = new User();
        // user.setEmail("cornelius.lilge@gmail.com");
        // user.setFirstname("cornelius");
        // post.setAuthor(user);
        // postService.addPost(post);
        // LOG.debug("List:");
        LOG.debug(postService.getPosts());
        for (final Post post : postService.getPosts()) {
            postService.deletePost(post);
        }
        // LOG.debug("List:");
        // LOG.debug(postService.getPosts());

        Spark.get(new IndexRoute("/"));

        // Upgrade given auth code to token, and store it in the session.
        // POST body of request should be the authorization code.
        // Example URI: /connect?state=...&gplus_id=...
        post(new ConnectRoute("/connect"));

        // Revoke current user's token and reset their session.
        post(new DisconnectRoute("/disconnect"));

        // Get list of people user has shared with this app.
        get(new PeopleRoute("/people"));
    }
}
