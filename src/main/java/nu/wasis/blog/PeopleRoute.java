package nu.wasis.blog;

import java.io.IOException;

import nu.wasis.util.PrivateConstants;
import spark.Request;
import spark.Response;
import spark.Route;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.PeopleFeed;

final class PeopleRoute extends Route {
    PeopleRoute(String path) {
        super(path);
    }

    @Override
    public Object handle(final Request request, final Response response) {
        response.type("application/json");
        // Only fetch a list of people for connected users.
        final String tokenData = request.session().attribute("token");
        if (tokenData == null) {
            response.status(401);
            return Blog.GSON.toJson("Current user not connected.");
        }
        try {
            // Build credential from stored token data.
            final GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(Blog.JSON_FACTORY)
                                                                              .setTransport(Blog.TRANSPORT)
                                                                              .setClientSecrets(PrivateConstants.CLIENT_ID,
                                                                                                PrivateConstants.CLIENT_SECRET)
                                                                              .build()
                                                                              .setFromTokenResponse(Blog.JSON_FACTORY.fromString(tokenData,
                                                                                                                            GoogleTokenResponse.class));
            // Create a new authorized API client.
            final Plus service = new Plus.Builder(Blog.TRANSPORT, Blog.JSON_FACTORY, credential).setApplicationName(PrivateConstants.APPLICATION_NAME)
                                                                                      .build();
            // Get a list of people that this user has shared with this app.
            final PeopleFeed people = service.people().list("me", "visible").execute();
            return Blog.GSON.toJson(people);
        } catch (final IOException e) {
            response.status(500);
            return Blog.GSON.toJson("Failed to read data from Google. " + e.getMessage());
        }
    }
}