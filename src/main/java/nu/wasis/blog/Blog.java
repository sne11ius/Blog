package nu.wasis.blog;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import nu.wasis.blog.model.Post;
import nu.wasis.blog.model.User;
import nu.wasis.service.PostService;
import nu.wasis.util.PrivateConstants;

import org.apache.log4j.Logger;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.PeopleFeed;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Blog {

    private static final Logger LOG = Logger.getLogger(Blog.class);
    private static PostService postService = new PostService();

    /**
     * Default HTTP transport to use to make HTTP requests.
     */
    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    /**
     * Default JSON factory to use to deserialize JSON.
     */
    private static final JacksonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Gson object to serialize JSON responses to requests to this servlet.
     */
    private static final Gson GSON = new Gson();

    /**
     * @param args
     */
    public static void main(final String[] args) {
        // MorphiaLoggerFactory.registerLogger(SLF4JLoggerImplFactory.class); System.setProperty("DB.TRACE", "true");
        final Post post = new Post();
        post.setBody("post body ftw!");
        post.setTitle("post title ftw!");
        final User user = new User();
        user.setEmail("cornelius.lilge@gmail.com");
        user.setFirstname("cornelius");
        post.setAuthor(user);
        postService.addPost(post);
        LOG.debug("List:");
        LOG.debug(postService.getPosts());
        // for (final Post post : postService.getPosts()) {
        // // postService.deletePost(post);
        // }
        LOG.debug("List:");
        LOG.debug(postService.getPosts());

        Spark.get(new Route("/") {
            @Override
            public Object handle(final Request request, final Response response) {
                final Configuration cfg = new Configuration();
                cfg.setClassForTemplateLoading(Blog.class, "/templates");
                cfg.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);
                final String state = new BigInteger(130, new SecureRandom()).toString(32);
                request.session().attribute("state", state);
                final StringWriter writer = new StringWriter();
                try {
                    final Template template = cfg.getTemplate("index.ftl");
                    final Map<String, Object> map = new HashMap<String, Object>();
                    map.put("posts", postService.getPosts());
                    map.put("client_id", PrivateConstants.CLIENT_ID);
                    map.put("state", state);
                    template.process(map, writer);
                } catch (final IOException e) {
                    LOG.error(e);
                } catch (final TemplateException e) {
                    LOG.error(e);
                }
                return writer;
            }
        });

        // Upgrade given auth code to token, and store it in the session.
        // POST body of request should be the authorization code.
        // Example URI: /connect?state=...&gplus_id=...
        post(new Route("/connect") {
            @Override
            public Object handle(final Request request, final Response response) {
                response.type("application/json");
                // Only connect a user that is not already connected.
                final String tokenData = request.session().attribute("token");
                if (tokenData != null) {
                    response.status(400);
                    return GSON.toJson("Current user is already connected.");
                }
                // Ensure that this is no request forgery going on, and that the user
                // sending us this connect request is the user that was supposed to.
                if (!request.queryParams("state").equals(request.session().attribute("state"))) {
                    response.status(401);
                    return GSON.toJson("Invalid state parameter.");
                }
                // Normally the state would be a one-time use token, however in our
                // simple case, we want a user to be able to connect and disconnect
                // without reloading the page. Thus, for demonstration, we don't
                // implement this best practice.
                // request.session().removeAttribute("state");

                final String gPlusId = request.queryParams("gplus_id");
                final String code = request.body();

                try {
                    // Upgrade the authorization code into an access and refresh token.
                    final GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                                                                                                      TRANSPORT,
                                                                                                      JSON_FACTORY,
                                                                                                      PrivateConstants.CLIENT_ID,
                                                                                                      PrivateConstants.CLIENT_SECRET,
                                                                                                      code,
                                                                                                      "postmessage").execute();
                    // Create a credential representation of the token data.
                    final GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JSON_FACTORY)
                                                                                      .setTransport(TRANSPORT)
                                                                                      .setClientSecrets(PrivateConstants.CLIENT_ID,
                                                                                                        PrivateConstants.CLIENT_SECRET)
                                                                                      .build()
                                                                                      .setFromTokenResponse(tokenResponse);

                    // Check that the token is valid.
                    final Oauth2 oauth2 = new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential).build();
                    final Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(credential.getAccessToken())
                                                      .execute();
                    // If there was an error in the token info, abort.
                    if (tokenInfo.containsKey("error")) {
                        response.status(401);
                        return GSON.toJson(tokenInfo.get("error").toString());
                    }
                    // Make sure the token we got is for the intended user.
                    if (!tokenInfo.getUserId().equals(gPlusId)) {
                        response.status(401);
                        return GSON.toJson("Token's user ID doesn't match given user ID.");
                    }
                    // Make sure the token we got is for our app.
                    if (!tokenInfo.getIssuedTo().equals(PrivateConstants.CLIENT_ID)) {
                        response.status(401);
                        return GSON.toJson("Token's client ID does not match app's.");
                    }
                    // Store the token in the session for later use.
                    request.session().attribute("token", tokenResponse.toString());
                    return GSON.toJson("Successfully connected user.");
                } catch (final TokenResponseException e) {
                    response.status(500);
                    return GSON.toJson("Failed to upgrade the authorization code.");
                } catch (final IOException e) {
                    response.status(500);
                    return GSON.toJson("Failed to read token data from Google. " + e.getMessage());
                }
            }
        });
        // Revoke current user's token and reset their session.
        post(new Route("/disconnect") {
            @Override
            public Object handle(final Request request, final Response response) {
                response.type("application/json");
                // Only disconnect a connected user.
                final String tokenData = request.session().attribute("token");
                if (tokenData == null) {
                    response.status(401);
                    return GSON.toJson("Current user not connected.");
                }
                try {
                    // Build credential from stored token data.
                    final GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JSON_FACTORY)
                                                                                      .setTransport(TRANSPORT)
                                                                                      .setClientSecrets(PrivateConstants.CLIENT_ID,
                                                                                                        PrivateConstants.CLIENT_SECRET)
                                                                                      .build()
                                                                                      .setFromTokenResponse(JSON_FACTORY.fromString(tokenData,
                                                                                                                                    GoogleTokenResponse.class));
                    // Execute HTTP GET request to revoke current token.
                    final HttpResponse revokeResponse = TRANSPORT.createRequestFactory()
                                                                 .buildGetRequest(new GenericUrl(
                                                                                                 String.format("https://accounts.google.com/o/oauth2/revoke?token=%s",
                                                                                                               credential.getAccessToken())))
                                                                 .execute();
                    // Reset the user's session.
                    request.session().removeAttribute("token");
                    return GSON.toJson("Successfully disconnected.");
                } catch (final IOException e) {
                    // For whatever reason, the given token was invalid.
                    response.status(400);
                    return GSON.toJson("Failed to revoke token for given user.");
                }
            }
        });
        // Get list of people user has shared with this app.
        get(new Route("/people") {
            @Override
            public Object handle(final Request request, final Response response) {
                response.type("application/json");
                // Only fetch a list of people for connected users.
                final String tokenData = request.session().attribute("token");
                if (tokenData == null) {
                    response.status(401);
                    return GSON.toJson("Current user not connected.");
                }
                try {
                    // Build credential from stored token data.
                    final GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JSON_FACTORY)
                                                                                      .setTransport(TRANSPORT)
                                                                                      .setClientSecrets(PrivateConstants.CLIENT_ID,
                                                                                                        PrivateConstants.CLIENT_SECRET)
                                                                                      .build()
                                                                                      .setFromTokenResponse(JSON_FACTORY.fromString(tokenData,
                                                                                                                                    GoogleTokenResponse.class));
                    // Create a new authorized API client.
                    final Plus service = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential).setApplicationName(PrivateConstants.APPLICATION_NAME)
                                                                                              .build();
                    // Get a list of people that this user has shared with this app.
                    final PeopleFeed people = service.people().list("me", "visible").execute();
                    return GSON.toJson(people);
                } catch (final IOException e) {
                    response.status(500);
                    return GSON.toJson("Failed to read data from Google. " + e.getMessage());
                }
            }
        });
    }
}
