package nu.wasis.blog;

import nu.wasis.service.PostService;

import org.apache.log4j.Logger;

import spark.Spark;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.Gson;

public class Blog {

    static final Logger LOG = Logger.getLogger(Blog.class);
    static final PostService postService = new PostService();

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
        Spark.get(new IndexRoute("/"));
        Spark.post(new ConnectRoute("/connect"));
        Spark.post(new DisconnectRoute("/disconnect"));
    }
}
