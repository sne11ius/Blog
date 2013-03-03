package nu.wasis.util;

import java.io.IOException;

import org.apache.log4j.Logger;

import spark.Request;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.google.gson.Gson;

public class GPlusUtils {

    private static final Logger LOG = Logger.getLogger(GPlusUtils.class);

    /**
     * Default HTTP transport to use to make HTTP requests.
     */
    public static final HttpTransport TRANSPORT = new NetHttpTransport();

    /**
     * Default JSON factory to use to deserialize JSON.
     */
    public static final JacksonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Gson object to serialize JSON responses to requests to this servlet.
     */
    public static final Gson GSON = new Gson();

    public static boolean isLoggedIn(final Request request) {
        return null != request.session().attribute("token");
    }

    public static String getCurrentUsername(final Request request) {
        final String tokenData = request.session().attribute("token");
        if (tokenData == null) {
            LOG.error("Not logged in.");
            return "[unknown]";
        }
        try {
            final GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JSON_FACTORY)
                                                                              .setTransport(TRANSPORT)
                                                                              .setClientSecrets(PrivateConstants.CLIENT_ID,
                                                                                                PrivateConstants.CLIENT_SECRET)
                                                                              .build()
                                                                              .setFromTokenResponse(JSON_FACTORY.fromString(tokenData,
                                                                                                                            GoogleTokenResponse.class));
            final Plus service = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential).setApplicationName(PrivateConstants.APPLICATION_NAME)
                                                                                      .build();
            final Person me = service.people().get("me").execute();
            return me.getDisplayName();
        } catch (final IOException e) {
            LOG.error(e);
            return "[unknown]";
        }
    }
}
